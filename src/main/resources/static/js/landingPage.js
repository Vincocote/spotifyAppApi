function landingPage() {

        fetch("http://localhost:8080/api/login")
        .then((response) => response.text())
        .then(response => {
            window.location.replace(response);
        })

}

function hello() {
    alert("hello");
}