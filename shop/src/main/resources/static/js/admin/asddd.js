$("#openModalButton").click(function() {
event.preventDefault();

var selectElement = document.querySelector("#myModal select[name='size']");
var selectedValue = selectElement.value;

    if (selectedValue.length === 0) {
        alert("선택된 사이즈가 없습니다.");
        return;
    }

var quantityInput = document.getElementById("quantity");

goodsSpan.textContent = quantityValue;



  /*  var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                // API 엔드포인트
                var apiUrl = '/admin/changeStatus';

                // API 요청 데이터
                var requestData = {

                 };

                fetch(apiUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        [csrfHeader]: csrfToken
                    },
                    body: JSON.stringify(requestData) // 요청 데이터를 JSON 문자열로 변환하여 body에 추가
                })
                .then(response => {
                    if (response.ok) {
                        alert("상태 변경 되었습니다");
                        location.reload();
                    } else {
                              response.text().then(errorMessage => {
                                  alert(errorMessage);
                              });
                          }
                })
                .catch(error => {
                    // 오류 처리
                    alert('Error:', error);
                });*/

                $("#myModal").css("display", "block");

})