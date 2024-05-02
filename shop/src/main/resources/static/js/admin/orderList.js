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
        location.href = '/admin/orders';
    } else {
        // 페이지 번호가 있는 경우
        location.href = '/admin/orders/' + page;
    }
}