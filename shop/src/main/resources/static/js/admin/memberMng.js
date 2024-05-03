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

                    location.href="/admin/members/" + page + "?searchDateType=" + searchDateType
                        + "&searchBy=" + searchBy
                        + "&searchQuery=" + searchQuery;
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


