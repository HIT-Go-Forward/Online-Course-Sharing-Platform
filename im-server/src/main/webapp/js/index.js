let Page = (function () {
    let ws;

    let $msg;
    let $send;
    let $connect;
    return {
        init() {
            $msg = $("#msg");
            $send = $("#send");
            $connect = $("#connect");

            $connect.click(() => {
                this.connect();
            });

            $send.click(() => {
                this.send($msg.val());
            })
        },

        connect() {
            if (!ws) {
                ws = new WebSocket("ws://localhost:55555/connectServer.action");
                ws.onmessage = function (msg) {
                    console.log(msg);
                    console.log(new Date() + ":" + msg.data);
                };
                ws.onopen = () => {
                    alert("connect succeeded~");
                }
            }
        },
        send(msg) {
            ws.send(msg);
        }
    }
})();