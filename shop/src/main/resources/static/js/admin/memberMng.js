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
//
//$(document).ready(function(){
//    // 페이지 로딩 후에도 실행될 함수
//    function handleLevel1Change() {
//        var selectedCategory = $('#level1').val();
//        var subCategory = $('#level2');
//
//        var level2Value = $("#level2value").text();
//        console.log(level2Value);
//        // 하위 카테고리들을 초기화합니다.
//        subCategory.empty();
//
//        // 선택된 상위 카테고리에 따라 하위 카테고리를 설정합니다.
//        switch (selectedCategory) {
//            case "Top":
//                subCategory.show();
//                subCategory.append($('<option>', {value:"", text: '하위카테고리 선택 (미 선택 시 전체검색)'}));
//                subCategory.append($('<option>', {value: '후드/블라우스', text: '블라우스'}));
//                subCategory.append($('<option>', {value: '셔츠', text: '셔츠'}));
//                subCategory.append($('<option>', {value: '맨투맨', text: '맨투맨/후드'}));
//                subCategory.append($('<option>', {value: '니트/스웨터', text: '니트/스웨터'}));
//                break;
//            case "Outer":
//                subCategory.show();
//                subCategory.append($('<option>', {value:"", text: '하위카테고리 선택 (미 선택 시 전체검색)'}));
//                subCategory.append($('<option>', {value: '패딩', text: '패딩'}));
//                subCategory.append($('<option>', {value: '코트', text: '코트'}));
//                subCategory.append($('<option>', {value: '재킷', text: '재킷'}));
//                subCategory.append($('<option>', {value: '집업', text: '집업'}));
//                break;
//            case "Pants":
//                subCategory.show();
//                subCategory.append($('<option>', {value:"", text: '하위카테고리 선택 (미 선택 시 전체검색)'}));
//                subCategory.append($('<option>', {value: '슬랙스', text: '슬랙스'}));
//                subCategory.append($('<option>', {value: '청바지', text: '청바지'}));
//                subCategory.append($('<option>', {value: '트레이닝/레깅스', text: '트레이닝/레깅스'}));
//                break;
//            case "Skirt/Dress":
//                subCategory.show();
//                subCategory.append($('<option>', {value:"", text: '하위카테고리 선택 (미 선택 시 전체검색)'}));
//                subCategory.append($('<option>', {value: '미니', text: '미니'}));
//                subCategory.append($('<option>', {value: '미디', text: '미디'}));
//                subCategory.append($('<option>', {value: '롱', text: '롱'}));
//                break;
//            case "Shoes":
//                subCategory.show();
//                subCategory.append($('<option>', {value:"", text: '하위카테고리 선택 (미 선택 시 전체검색)'}));
//                subCategory.append($('<option>', {value: '부츠', text: '부츠'}));
//                subCategory.append($('<option>', {value: '힐/로퍼', text: '힐/로퍼'}));
//                subCategory.append($('<option>', {value: '운동화', text: '운동화'}));
//                subCategory.append($('<option>', {value: '슬리퍼', text: '슬리퍼'}));
//                break;
//            default:
//                subCategory.hide();
//                break;
//        }
//        if (level2Value != null) {
//            $("#level2").val(level2Value);
//        } else{
//        $('#level2').val($('#level2 option:first').val());
//        }
//
//    }
//
//    // 페이지 로딩 시 실행
//    handleLevel1Change();
//
//    // #level1 값 변경 시 실행
//    $('#level1').change(handleLevel1Change);
//});
//
//                $(document).ready(function(){
//            $('#searchBtn').on("click", function(e){
//                e.preventDefault();
//                page(0);
//            })
//        });
//
//        //선택되지 않앗을 시 null 적용.
//        function page(page){
//            const searchDateType = $("#searchDateType").val();
//            const level1 = $("#level1").val();
//            const level2 = $("#level2").val() || null;
//            const searchBy = $("#searchBy").val();
//            const searchQuery = $("#searchQuery").val();
//            const itemSellStatus = $("#itemSellStatus").val();  // 판매 상태
//
//            location.href="/admin/items/" + page + "?searchDateType=" + searchDateType
//                + "&level1=" + level1
//                + "&level2=" + level2
//                + "&searchBy=" + searchBy
//                + "&searchQuery=" + searchQuery
//                + "&itemSellStatus=" + itemSellStatus;
//        }

$(document).ready(function(){
    $('#searchBtn').on("click", function(e){
        e.preventDefault();
        page(0);
    })
});

// 선택되지 않았을 시 null 적용.
function page(page) {
    if (page === null || page === undefined) {
        // 페이지 번호가 없는 경우 또는 null인 경우
        location.href = '/admin/members';
    } else {
        // 페이지 번호가 있는 경우
        location.href = '/admin/members/' + page;
    }
}