$("#login").on('click', function () {
    alert("aaa");
    $.ajax({
        url: "/login/login.do",
        type: "POST",
        success: function (result) {
            alert(result);
        }
    });
});

// $("#testToken").on('click', function () {
//    $.ajax({
//        url: "/test/index.html",
//        type: "GET",
//    });
// });