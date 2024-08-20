const roomId = 176;
// const userId = "0190964c-af3f-7486-8ac3-d3ff10cc1470"; // test1
const userId = "0190964c-ee3a-7e81-a1f8-231b5d97c2a1"; // test4
const domain = 'dev-api.ranchat.net';
// const domain = 'localhost:8080';
const brokerUrl = `ws://${domain}/endpoint`;
// const brokerUrl = `ws://${domain}/endpoint`;


const stompClient = new StompJs.Client({
    brokerURL: brokerUrl,
    debug: function (str) {
        console.log(str);
    },
    connectHeaders: {
        userId: userId
    }
});

stompClient.onConnect = async (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    await stompClient.subscribe(`/queue/v1/errors`, (response) => {
        console.log("greeting: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    await stompClient.subscribe(`/user/queue/v1/errors`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    await stompClient.subscribe(`/${userId}/queue/v1/errors`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });


    await stompClient.subscribe(`/user/${userId}/queue/v1/errors`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    await stompClient.subscribe(`/user/queue/v1/matching/success`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    await stompClient.subscribe(`/user/${userId}/queue/v1/matching/success`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    await stompClient.subscribe(`/topic/v1/rooms/${roomId}/messages/new`, (response) => {
        console.log("response: ", response);
        const roomId = JSON.parse(response.body);
        console.log("roomId: ", roomId);
    });

    // await createUser();
    // const messages = await fetchMessages(roomId);
    //
    // messages.forEach((message) => {
    //     addMessage(message);
    // });

    const payload = {
        roomId,
        userId
    }

    // await stompClient.publish({
    //     destination: "/app/enter",
    //     body: JSON.stringify(payload)
    // });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

async function connect() {
    await stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    const payload = {
        "userId": userId,
        "content": "안녕 뭐하고 있어?",
        "contentType": "TEXT",
    }

    stompClient.publish({
        destination: `/v1/rooms/${roomId}/messages/send`,
        headers: {},
        body: JSON.stringify(payload)
    });
}

function test() {
    const userId = {
        "userId": "0190964c-ee3a-7e81-a1f8-231b5d97c2a1"
    };

    stompClient.publish({
        destination: "/v1/rooms/174/exit",
        headers: {},
        body: JSON.stringify(userId)
    });
}


function addMessage(message) {
    const str = message.userId.split('-')[0] + ' : ' + message.content;
    $("#greetings").append("<tr><td>" + str + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    $( "#test" ).click(() => test());
});

async function fetchMessages(roomId) {
    try {
        const endpoint = `https://${domain}/v1/rooms/${roomId}/messages`;
        const response = await fetch(endpoint);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error fetching messages:', error);
        return [];
    }
}

async function createUser() {
    try {
        const endpoint = `https://${domain}/v1/users`;
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ id: userId, name: userId })
        });
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
    } catch (error) {
        console.error('Error creating user:', error);
        return {};
    }
}