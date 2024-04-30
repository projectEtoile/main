$(document).ready(function() {
    document.getElementById("sendEmailBtn").addEventListener("click", function(event) {
        sendMail(event);
    });
});
function sendMail(event) {
    event.preventDefault(); // 폼의 기본 동작인 페이지 리로드를 막음

    var email = $("#memberEmail").val(); // 이메일 입력란의 값 가져오기

    if (email.trim() !== "") { // 이메일이 입력되었는지 확인
        var requestData = {
            "memberEmail": email // 사용자가 입력한 이메일 주소
        };

        // CSRF 토큰 가져오기
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // AJAX 요청 설정
        $.ajax({
            type: "POST",
            url: "/members/findPassword",

            contentType: "application/json",
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰 설정
            },
            data: JSON.stringify(requestData), // 이메일 데이터를 JSON 문자열로 전송
            success: function(response) {
                // 이메일 전송에 성공한 경우
                alert("이메일이 전송되었습니다.");
            },
            error: function(xhr, status, error) {
                // 이메일 전송에 실패한 경우
                alert("이메일 전송에 실패했습니다.");
                console.error("이메일 전송에 실패했습니다.", error);
            }
        });
    } else {
        alert("이메일을 입력하세요."); // 이메일이 입력되지 않았을 때 알림 메시지 출력
    }

    function verifyCode() {
        var verificationCode = document.getElementById("verificationCode").value;
        // Ajax를 사용하여 서버에 인증번호를 전송하고 확인하는 작업 수행

        // 임시로 인증번호가 일치한다고 가정
        var isCodeCorrect = true;

        if (isCodeCorrect) {
            document.getElementById("verificationMessage").innerText = "인증번호가 일치합니다.";
            document.getElementById("verifyCodeForm").style.display = "none"; // 인증번호 입력 폼 숨기기
            document.getElementById("changePasswordForm").style.display = "block"; // 비밀번호 변경 폼 표시
        } else {
            alert("인증번호가 일치하지 않습니다. 다시 시도해주세요.");
        }
    }
}
