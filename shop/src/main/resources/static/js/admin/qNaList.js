$(document).ready(function() {
    // 페이지 로드 시 실행
    var scrollPosition = localStorage.getItem('scrollPosition');
    if (scrollPosition !== null) {
        $(window).scrollTop(scrollPosition);
    }

    // 페이지 이동 버튼 클릭 시 실행
    $('.pagination-link').click(function() {
        // 현재 스크롤 위치를 localStorage에 저장
        var currentScrollPosition = $(window).scrollTop();
        localStorage.setItem('scrollPosition', currentScrollPosition);
    });

    // 페이지가 새로고침 될 때 실행
    $(window).on('beforeunload', function() {
        // 현재 스크롤 위치를 localStorage에 저장
        var currentScrollPosition = $(window).scrollTop();
        localStorage.setItem('scrollPosition', currentScrollPosition);
    });
});

  $(document).ready(function(){
            $('#searchBtn').on("click", function(e){
                e.preventDefault();
                page(0);
            })
        });

                function page(page){
                    const searchDateType = $("#searchDateType").val();
                    const searchBy = $("#searchBy").val();
                    const searchQuery = $("#searchQuery").val();
                    const answerStatus = $("#answerStatus").val();


                    location.href="/admin/qNaLists/" + page + "?searchDateType=" + searchDateType
                        + "&searchBy=" + searchBy
                        + "&searchQuery=" + searchQuery
                        + "&answerStatus=" + answerStatus;
                }

//                $(document).ready(function(){
//                    $('#searchBtn').on("click", function(e){
//                        e.preventDefault();
//                        page(0);
//                    })
//                });
//
//                // 선택되지 않았을 시 null 적용.
//                function page(page) {
//                    if (page === null || page === undefined) {
//                        // 페이지 번호가 없는 경우 또는 null인 경우
//                        location.href = '/admin/members';
//                    } else {
//                        // 페이지 번호가 있는 경우
//                        location.href = '/admin/members/' + page;
//                    }
//                }

// 문의 모달 요소 가져오기
// 문의 모달 요소 가져오기
var modal = $(document).find("#inquiryModal")[0];

// 모달 열기 함수
function openModal(id) {
     // window.openModalBtnModify = function(id) {

            var csrfToken = $("meta[name='_csrf']").attr("content");
            var csrfHeader = $("meta[name='_csrf_header']").attr("content");

            // REST API 엔드포인트 URL
            var apiUrl = "/admin/qNa";

            // AJAX 요청
            $.ajax({
                type: "GET",
                url: apiUrl + "?id=" + id,
                beforeSend: function(xhr) {
                    // CSRF 토큰을 헤더에 포함
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function(response) {
                var data = JSON.parse(response);

     $("#id").val(data.id);

     $("#question").val(data.question);
     $("#title").text(data.title);
     $("#answer").val(data.answer);

    $("#inquiryModal").css("display", "block");
                },
                error: function(xhr, status, error) {
                    // 요청이 실패했을 때 실행할 코드
                    alert("REST API 요청 실패:", error);
                }
            });


        };

function answerFinished(){
           var csrfToken = $("meta[name='_csrf']").attr("content");
                    var csrfHeader = $("meta[name='_csrf_header']").attr("content");

                    // REST API 엔드포인트 URL
                    var apiUrl = "/admin/qNa";

                        var answerDate = {
                            id: document.getElementById("id").value,
                            answer: document.getElementById("answer").value
                        };


                    // AJAX 요청
                    $.ajax({
                        type: "PUT",
                        url: apiUrl,
                        beforeSend: function(xhr) {
                            // CSRF 토큰을 헤더에 포함
                            xhr.setRequestHeader(csrfHeader, csrfToken);
                        },
                        contentType: "application/json",
                            dataType: "text",
                        data: JSON.stringify(answerDate),
                        success: function(response) {

                        alert(response);
                        location.reload();


                        },
                        error: function(xhr, status, error) {
                            // 요청이 실패했을 때 실행할 코드
                            alert("REST API 요청 실패: " + error);
                        }
                    });
}


// 모달 닫기 함수
function closeModal() {
     $("#inquiryModal").css("display", "none");
}

// 답변 제출 함수
function submitResponse() {
    var responseInput = document.getElementById("responseInput");
    var response = responseInput.value; // 입력된 답변 가져오기
    alert("답변이 제출되었습니다: " + response);
    closeModal(); // 모달 닫기
}
