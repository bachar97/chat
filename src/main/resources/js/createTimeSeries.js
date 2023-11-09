document.addEventListener("DOMContentLoaded", ()=>{
    const createButton = document.getElementById("createButton");
    createButton.addEventListener("click", createTimeSeries);
});

function createTimeSeries(){
    const name = document.getElementById("name");
    const description = document.getElementById("description");

    const eventData = {
        name,
        description
    }

    fetch("api/timeseries", {
        method: "POST",
        headers: {
            "Content-Type": "text/html"
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
