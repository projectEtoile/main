    document.addEventListener("DOMContentLoaded", function() {
        var dropdownButtons = document.querySelectorAll(".dropdown-btn");

        dropdownButtons.forEach(function(button) {
            button.addEventListener("click", function() {
                var dropdownContent = this.nextElementSibling;
                dropdownContent.style.display = (dropdownContent.style.display === "block") ? "none" : "block";
            });
        });
    });