$(document).ready(function() {
    $("input[name=cartChkBox]").change(function() {
        getOrderTotalPrice();
    });
});

function getOrderTotalPrice() {
    let orderTotalPrice = 0;
    $("input[name=cartChkBox]:checked").each(function () {
        const cartItemId = $(this).val();  // 체크박스 값으로부터 cartItem ID를 가져옵니다.
        const priceElement = $("#price_" + cartItemId);  // 해당 상품의 가격 요소를 선택합니다.
        const itemPrice = parseFloat(priceElement.text().replace('원', '').replace(/,/g, ''));  // 가격에서 '원'과 ','를 제거하고 숫자로 변환합니다.
        orderTotalPrice += itemPrice;  // 전체 가격에 합산합니다.
    });
    $("#orderTotalPrice").text(orderTotalPrice.toLocaleString() + '원');  // 총 금액을 화면에 표시합니다.
}



function changeCount(obj) {
    const count = parseInt(obj.value);  // 변경된 수량을 가져옵니다.
    const cartItemId = obj.id.split("_")[1];  // 상품 ID를 추출합니다.
    const priceElement = $("#price_" + cartItemId);  // 가격을 표시하는 요소를 선택합니다.
    const price = parseFloat(priceElement.data('price'));  // 원래 가격을 data-price 속성에서 가져옵니다.

    const totalPrice = count * price;  // 새로운 총 가격을 계산합니다.
    priceElement.text(totalPrice.toLocaleString() + "원");  // 화면에 새로운 가격을 표시합니다.

    getOrderTotalPrice();  // 전체 주문 금액을 다시 계산하여 업데이트합니다.
}

function checkAll() {
    const isAllChecked = $("#checkAll").prop("checked");
    $("input[name=cartChkBox]").prop("checked", isAllChecked);
    getOrderTotalPrice();
}

function updateCartItemCount(cartItemId, count){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const url = "/cartItem/" + cartItemId + "?count=" + count;
    $.ajax({
        url:url,
        type:"PATCH",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        cache:false,
        success:function(result, status){
            console.log("cartItem count update success");
        },
        error:function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert("로그인 후 이용해주세요.");
                location.href = '/member/login';
            } else {
                alert(jqXHR.responseText);
            }
        }
    });
}

function deleteCartItem(obj){
    const cartItemId = obj.dataset.id;
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    const url = "/cartItem/" + cartItemId;

    $.ajax({
        url:url,
        type:"DELETE",
        beforeSend:function(xhr){
            xhr.setRequestHeader(header, token);
        },
        dataType:"json",
        cache:false,
        success:function(result, status){
            alert("장바구니 상품을 삭제 했습니다.");
            location.href='/cart';
        },
        error:function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert("로그인 후 이용해주세요.");
                location.href='/member/login';
            } else {
                alert(jqXHR.responseJSON.message);
            }
        }
    });
}

$(document).ready(function() {
    updateCartItemCount();
});

function updateCartItemCount() {
    $.ajax({
        url: '/cart/count', // 서버의 API 경로
        type: 'GET',
        success: function(count) {
            $('#cartItemCount').text(count); // 받아온 수량으로 업데이트
        },
        error: function(xhr, status, error) {
            console.error("장바구니 상품 수를 가져오는 데 실패했습니다:", xhr.responseJSON);
        }
    });
}





function removeSelectedItems() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    // 체크된 장바구니 상품들을 배열로 저장
    const checkedItems = $("input[name=cartChkBox]:checked").map(function () {
        return $(this).val();
    }).get();

    if (checkedItems.length === 0) {
        alert("삭제할 상품을 선택하세요.");
        return;
    }

    if (confirm("선택한 상품을 삭제하시겠습니까?")) {
        // 선택한 상품들에 대해 삭제 요청 보내기
        checkedItems.forEach(function (cartItemId) {
            const url = "/cartItem/" + cartItemId;
            $.ajax({
                url: url,
                type: "DELETE",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                dataType: "json",
                cache: false,
                success: function (result, status) {
                    console.log("장바구니 상품을 삭제했습니다.");
                    // 상품 삭제 후 페이지 새로고침
                    location.reload();
                },
                error: function (jqXHR, status, error) {
                    if (jqXHR.status == '401') {
                        alert("로그인 후 이용해주세요.");
                        location.href = '/member/login';
                    } else {
                        alert(jqXHR.responseJSON.message);
                    }
                }
            });
        });
    }
}



function orders() {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    let cartOrders = [];

    $("input[name=cartChkBox]:checked").each(function() {
        const cartItemId = $(this).val();
        const count = $("#count_" + cartItemId).val();
        cartOrders.push({ cartItemId: cartItemId, count: parseInt(count) });
    });

    if (cartOrders.length === 0) {
        alert("주문할 상품을 선택해주세요.");
        return;
    }

    const paramData = JSON.stringify({ cartOrderDTOList: cartOrders });

    $.ajax({
        url: "/cart/orders",
        type: "POST",
        contentType: "application/json",
        data: paramData,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function(response) {
            alert("모든 주문이 성공적으로 처리되었습니다.");
            location.reload();
        },
        error: function() {
            alert("주문 처리 중 오류가 발생했습니다.");
        }
    });
}



