
function registerAddress(event) {
event.preventDefault();

var selectAddress;

// 체크박스의 상태에 따라 selectAddress 값을 설정
if (document.getElementById("selectAddress").checked) {
    selectAddress = true;
} else {
    selectAddress = false;
}
console.log(selectAddress);

    // 주소 정보를 입력 받아서 객체로 만듭니다.
    var addressData = {
        postcode: document.getElementById("postcode").value,
        roadAddress: document.getElementById("roadAddress").value,
        jibunAddress: document.getElementById("jibunAddress").value,
        detailAddress: document.getElementById("detailAddress").value,
        extraAddress: document.getElementById("extraAddress").value,
        selectAddress: selectAddress
    };
console.log(addressData);
    // CSRF 토큰 가져오기
    var csrfToken = $("meta[name='_csrf']").attr("content");
    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // AJAX 요청 설정
    $.ajax({
        type: "POST",
        url: "/mypage/addAddress", // 서버의 엔드포인트 URL
        contentType: "application/json",
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken); // CSRF 토큰을 요청 헤더에 포함
        },
        data: JSON.stringify(addressData), // 주소 정보를 JSON 문자열로 변환하여 요청 본문에 포함
        success: function(response) {
            // 요청이 성공한 경우
            alert(response); // 서버에서 반환한 메시지를 알림으로 표시
            location.reload();
        },
        error: function(xhr, status, error) {
            // 요청이 실패한 경우
            alert("주소 등록에 실패했습니다.");
            console.error("주소 등록에 실패했습니다.", error);
        }
    });
}





$(document).ready(function() {
    // 모달 열기 함수
    window.openModalBtnModify = function(id) {

        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // REST API 엔드포인트 URL
        var apiUrl = "/mypage/modify";

        // AJAX 요청
        $.ajax({
            type: "GET",
            url: apiUrl + "?id=" + id,
            beforeSend: function(xhr) {
                // CSRF 토큰을 헤더에 포함
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
            var addressData = JSON.parse(response);

 $("#id").val(addressData.id);

 $("#postcode").val(addressData.postcode);
 $("#roadAddress").val(addressData.roadAddress);
 $("#jibunAddress").val(addressData.jibunAddress);
 $("#detailAddress").val(addressData.detailAddress);
 $("#extraAddress").val(addressData.extraAddress);

var bool = addressData.selectAddress;
console.log(bool);
 if(bool === true){
$("#selectAddress").prop("checked", true);
 }else{
 $("#selectAddress").prop("checked", false);
 }

 $("#regBtn").hide();
$("#modBtn").show();

            },
            error: function(xhr, status, error) {
                // 요청이 실패했을 때 실행할 코드
                alert("REST API 요청 실패:", error);
            }
        });


        $("#myModal").css("display", "block");
    };

    // 모달 닫기
    $(".close, .modal").click(function(event) {
        if (event.target == $("#myModal")[0] || event.target.className == "close") {
            $("#myModal").css("display", "none");
        }
    });
});

// 수정하기 비동기 통신

function modifyAddress(event) {
event.preventDefault();
            // CSRF 토큰 가져오기
            var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            // API 엔드포인트
            var apiUrl = '/mypage/modify';

            // API 요청 데이터
            var requestData = {
                id: document.getElementById("id").value,
                postcode: document.getElementById("postcode").value,
                            roadAddress: document.getElementById("roadAddress").value,
                            jibunAddress: document.getElementById("jibunAddress").value,
                            detailAddress: document.getElementById("detailAddress").value,
                            extraAddress: document.getElementById("extraAddress").value,
                            selectAddress: document.getElementById("selectAddress").checked
            };

            fetch(apiUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    [csrfHeader]: csrfToken
                },
                body: JSON.stringify(requestData) // 요청 데이터를 JSON 문자열로 변환하여 body에 추가
            })
            .then(response => {
                if (response.ok) {
                    alert("주소 수정에 성공했습니다.");
                    location.reload();
                } else {
                    alert("주소 수정에 실패했습니다.");
                }
            })
            .catch(error => {
                // 오류 처리
                alert('Error:', error);
            });
        }










$(document).ready(function() {


  // 모달 열기
  $("#openModalBtn").click(function() {

 $("#postcode").val('');
    $("#roadAddress").val('');
    $("#jibunAddress").val('');
    $("#detailAddress").val('');
    $("#extraAddress").val('');
    $("#selectAddress").prop("checked", false);

    $("#myModal").css("display", "block");
  });

  // 모달 닫기
  $(".close, .modal").click(function(event) {
    if (event.target == $("#myModal")[0] || event.target.className == "close") {
      $("#myModal").css("display", "none");
    }
  });
});

 function execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('postcode').value = data.zonecode;
                document.getElementById("roadAddress").value = roadAddr;
                document.getElementById("jibunAddress").value = data.jibunAddress;

                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }).open();
    }