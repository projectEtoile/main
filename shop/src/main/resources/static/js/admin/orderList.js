  $(document).ready(function(){
            $('#searchBtn').on("click", function(e){
                e.preventDefault();
                page(0);
            })
        });

// 선택되지 않았을 시 null 적용.
//function page(page) {
//    if (page === null || page === undefined) {
//        // 페이지 번호가 없는 경우 또는 null인 경우
//        location.href = '/admin/orders';
//    } else {
//        // 페이지 번호가 있는 경우
//        location.href = '/admin/orders/' + page;
//    }
//}
        function page(page){
            const searchDateType = $("#searchDateType").val();
            const searchBy = $("#searchBy").val();
            const searchQuery = $("#searchQuery").val();
            const orderStatus = $("#orderStatus").val();

            location.href="/admin/orders/" + page + "?searchDateType=" + searchDateType
                + "&searchBy=" + searchBy
                + "&searchQuery=" + searchQuery
                + "&orderStatus=" + orderStatus;
        }




function changeStatus(currentState,newState,event) {
event.preventDefault();
            // CSRF 토큰 가져오기
            var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            // API 엔드포인트
            var apiUrl = '/admin/changeStatus';

            // API 요청 데이터
            var requestData = {
                currentState : currentState,
                newState : newState
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
//                    location.reload();
                } else {
                    alert("상태 변경이 실패했습니다");
                }
            })
            .catch(error => {
                // 오류 처리
                alert('Error:', error);
            });
        }

//        $(document).ready(function() {
//            // 페이지 로드 시 실행
//            var scrollPosition = localStorage.getItem('scrollPosition');
//            if (scrollPosition !== null) {
//                $(window).scrollTop(scrollPosition);
//            }
//
//            // 페이지 이동 버튼 클릭 시 실행
//            $('.pagination-link').click(function() {
//                // 현재 스크롤 위치를 localStorage에 저장
//                var currentScrollPosition = $(window).scrollTop();
//                localStorage.setItem('scrollPosition', currentScrollPosition);
//            });
//
//            // 페이지가 새로고침 될 때 실행
//            $(window).on('beforeunload', function() {
//                // 현재 스크롤 위치를 localStorage에 저장
//                var currentScrollPosition = $(window).scrollTop();
//                localStorage.setItem('scrollPosition', currentScrollPosition);
//            });
//        });