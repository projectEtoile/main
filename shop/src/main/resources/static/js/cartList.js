$(document).ready(function(){
    $("input[name=cartChkBox]").change(function(){
        getOrderTotalPrice();
    });
});

function getOrderTotalPrice() {
    let orderTotalPrice = 0;
    $("input[name=cartChkBox]:checked").each(function () {
        const cartItemId = $(this).val();
        const price = parseFloat($("#price_" + cartItemId).text().replace('원', '').replace(',', ''));
        const count = parseInt($("#count_" + cartItemId).val());
        orderTotalPrice += price * count;
    });
    $("#orderTotalPrice").text(orderTotalPrice.toLocaleString() + '원');
}


function changeCount(obj){
            const count = obj.value;
            const cartItemId = obj.id.split("_")[1];
            const price = $("#price_" + cartItemId).data('price');
            const totalPrice = count * price;
            $("#totalPrice_" + cartItemId).html(totalPrice + "원");
            getOrderTotalPrice();
            updateCartItemCount(cartItemId, count);
        }
function checkAll(){
    if($("#checkAll").prop("checked")){
        $("input[name=cartChkBox]").prop("checked", true);
    } else {
        $("input[name=cartChkBox]").prop("checked", false);
    }
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



function orders(){
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");

    const url = "/cart/orders";
    const dataList = new Array();
    const paramData = new Object();

    // 체크된 장바구니의 상품 아이디를 전달하기 위해서
    // dataList배열에 장바구니 상품아이디를 객체로 만들어 저장함.
    $("input[name=cartChkBox]:checked").each(function(){
        const cartItemId = $(this).val();
        const data = new Object();
        data["cartItemId"] = cartItemId;
        dataList.push(data);
        console.log("===== dataList : " + dataList);
    });

    // 장바구니 상품 아이디를 저장해 둔 dataList배열을 paramData 객체에 추가함.
    paramData['cartOrderDTOList']= dataList;

    const param = JSON.stringify(paramData);

    $.ajax({
        url: url,
        type: "POST",
        contentType: "application/json",
        data: param,
        beforeSend: function(xhr){
            xhr.setRequestHeader(header, token);
        },
        dataType: "json",
        cache: false,
        success: function(result, status){
            alert("주문이 완료 되었습니다.");
            location.href = "/orders";
        },
        error: function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert("로그인 후 이용해주세요.");
                location.href= "/members/login";
            } else {
                alert(jqXHR.responseJSON.messages);
            }
        }
    });

}
