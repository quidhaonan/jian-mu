# WebSocket

## 1. 注意
1. 代码放入浏览器无痕模式下，不然会被拦截 CSP

## 2. WebSocket 连接
1. 假设 ip 地址为：`127.0.0.1`
    + 如服务器上运行，使用了 nginx，则地址默认加上 `/ws/jianmu`
      + ws 连接地址：`ws://127.0.0.1/ws/jianmu/ws/connect`
    + 如普通本地运行，则无默认前缀
      + ws 连接地址：`ws://127.0.0.1:9999/ws/connect`

2. 初始化

    ```json
    {
        "type":"init",
        "msgId":"",
        "data":{
            "clientId":""
        }
    }
    ```
    + type：请求类型
    + msgId：消息 id
    + clientId：设备 id

3. 心跳检测

    ```json
    {
        "type":"heartbeat",
        "msgId":"",
        "data":{
            
        }
    }
    ```
    + type：请求类型
    + msgId：消息 id



## 3. 浏览器加密

1. 浏览器进行初始化（需要修改：加密函数 e、websocket 地址 url、clientId（自定义字符串））

    ```js
    !(function () {
        if (window.lmy != undefined) {
            console.log('第' + window.lmy.index++ + '次执行，直接返回')
            return
        }
    
        window.lmy = { "index": 1 }
        // e：加密函数
        window.lmy.encrypt = e
        // url：ws 地址
        let url = ''
        let clientId = ''
    
        let ws
        // WebSocket 重连尝试的间隔时间（毫秒）
        const RECONNECT_TIME = 1000;
        // 心跳检测定时器
        let HEARTBEAT_INTERVAL
        // 重连定时器
        let RECONNECT_TIMEOUT
        // 是否重连
        let isReconnect = true
    
        // 创建 WebSocket 连接函数
        function connect() {
            if (HEARTBEAT_INTERVAL != undefined) {
                window.clearInterval(HEARTBEAT_INTERVAL)
            }
            if (RECONNECT_TIMEOUT != undefined) {
                window.clearTimeout(RECONNECT_TIMEOUT)
            }
            ws = new WebSocket(url);
    
            ws.onopen = function (event) {
                let initData = {
                    "type": "init",
                    "msgId": "",
                    "data": {
                        "clientId": clientId
                    }
                }
                ws.send(JSON.stringify(initData))
    
                HEARTBEAT_INTERVAL = setInterval(() => {
                    let heartData = {
                        "type": "heartbeat",
                        "msgId": '',
                        "data": {
    
                        }
                    }
                    ws.send(JSON.stringify(heartData))
                }, 1000 * 60 * 2)
            }
    
            ws.onmessage = function (event) {
                let data = JSON.parse(event.data)
                if (data.type == 'browser_encrypt') {
                    let browserEncryptData = {
                        "type": data.type,
                        "msgId": data.msgId,
                        "data": {
                            "clientId": data.data.clientId,
                            "ciphertext": window.lmy.encrypt(data.data.plaintext),
                            "randomStr": data.data.randomStr
                        }
                    }
                    ws.send(JSON.stringify(browserEncryptData))
                }
            }
    
            ws.onerror = function (error) {
                console.error('WebSocket error:', error);
            };
    
            ws.onclose = function () {
                console.error('WebSocket disconnected');
                // 等待一段时间后尝试重连
                if (isReconnect) {
                    setTimeout(connect, RECONNECT_TIME);
                }
            };
        }
    
        // 初次连接
        connect();
    
        // 停止重连
        window.lmy.stopReconnect = function stopReconnect() {
            isReconnect = false
        }
    })()
    ```



2.  发送 post 请求进行加密，假设 ip 地址为：`127.0.0.1` 
    + 如服务器上运行，使用了 nginx，则地址默认加上 `/api/jianmu`
      + 请求地址：`127.0.0.1/api/jianmu/ws/browser/encrypt`
    + 如普通本地运行，则无默认前缀
      + 请求地址：`127.0.0.1:9999/ws/browser/encrypt`
    + 参数
    
        ```json
        {
            "clientId":"",
            "plaintext":""
        }
        ```
        
        + clientId：随机字符串，需要与初始化 js 的 clientId 一一对应
        + plaintext：待加密的明文