document.addEventListener("DOMContentLoaded", ()=>{
    const createButton = document.getElementById("createButton");
    createButton.addEventListener("click", createEvent);
});

function createEvent(){
    const date = document.getElementById("date");
    const description = document.getElementById("description");

    const eventData = {
        date,
        description
    }

    fetch("api/event", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(eventData)
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            alert('Event created successfully!');
        })
        .catch((error) => {
            console.error('Error:', error);
            alert('Error creating event');
        });
}
