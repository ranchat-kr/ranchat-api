const roomId = 2;
const userId = Math.floor(Math.random() * 1000000) + '-' + (+new Date());
const domain = '52.78.91.184:8080';
// const domain = 'localhost:8080';

const stompClient = new StompJs.Client({
    brokerURL: `ws://${domain}/endpoint`
});

stompClient.onConnect = async (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);

    // await stompClient.subscribe('/v1/room/1/message/send', (greeting) => {
    //     console.log("greeting: ", greeting);
    //     const message = JSON.parse(greeting.body);
    //     addMessage(message);
    // });

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

function addMessage(message) {
    const str = message.userId.split('-')[0] + ' : ' + message.content;
    $("#greetings").append("<tr><td>" + str + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
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