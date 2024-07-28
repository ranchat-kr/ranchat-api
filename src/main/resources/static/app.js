const roomId = 1;
// const userId = "0190964c-af3f-7486-8ac3-d3ff10cc1470"; // test1
const userId = "0190de4d-d720-7206-b87e-85294ea96838"; // test4
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

    await stompClient.subscribe(`/topic/v1/matching/success`, (response) => {
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
        "userId": "0190964c-af3f-7486-8ac3-d3ff10cc1470",
        "content": "sex",
        "contentType": "TEXT",
    }

    stompClient.publish({
        destination: "/test"
    });

    stompClient.publish({
        destination: "/v1/rooms/1/messages/send",
        headers: {},
        body: JSON.stringify(payload)
    });
}

function test() {
    var userIds = [
        "0190964c-af3f-7486-8ac3-d3ff10cc1470",
        "0190964c-ee3a-7e81-a1f8-231b5d97c2a1",
        "0190de4d-b8da-7d8d-bc9c-880564b12e28",
        "0190de4d-d720-7206-b87e-85294ea96838"
    ];

    userIds.forEach(async id => {
        stompClient.publish({
            destination: "/v1/matching/apply",
            headers: {userId},
            body: JSON.stringify({userId: id})
        });

        for(let i = 0; i < 300000000; i++) {}
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