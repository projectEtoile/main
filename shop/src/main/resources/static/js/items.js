// 전역 변수 선언
var levelValue;
var searchQueryValue;

// 현재 페이지 URL에서 매개변수 값을 가져오는 함수
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

$(document).ready(function() {
    // 전역 변수에 값 할당
    levelValue = getParameterByName('level');
    searchQueryValue = getParameterByName('searchQuery');
    saleItemsValue = getParameterByName('saleItems');

     if(levelValue){
     $("#message").text(levelValue);
     }

     if(saleItemsValue){
        $("#message").text("할인 상품 목록");
     }

     if(!levelValue && !saleItemsValue && !searchQueryValue){
      $("#message").text("신제품 목록");
     }

    // 검색 버튼 클릭 시 페이지 이동 함수
    $('#searchBtn').on("click", function(e){
        e.preventDefault();
        page(0);
    });
});

// 페이지 이동 함수
function page(page){
    location.href="/items/" + page + "?level=" + levelValue
        + "&searchBy="
        + "itemNm"
        + "&saleItems="
        + "&searchQuery=" + searchQueryValue;
}
