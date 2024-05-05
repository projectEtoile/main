// 작은섬네일을 선택하면 큰사진이 변경

document.addEventListener('DOMContentLoaded', function() {
  var bigPic = document.getElementById("big_img");
  var smallPics = document.querySelectorAll(".img_thumb img");

  for (var i = 0; i < smallPics.length; i++) {
    smallPics[i].addEventListener("click", changepic);
  }

  function changepic(event) {
    event.preventDefault();

    var smallPicAttribute = this.getAttribute("src");
    bigPic.setAttribute("src", smallPicAttribute);

    for (var i = 0; i < smallPics.length; i++) {
      smallPics[i].style.opacity = "0.5";
    }

    this.style.opacity = "1.0";
  }
});

// 사이즈를선택하면 총금액박스가 나타남
function showTotalPrice() {
  var selectBox = document.getElementsByName("size")[0];
  var totalPriceDiv = document.getElementById("total_price");

  var selectedSize = selectBox.value;

  // "S", "M", "L", "Free" 중 하나를 선택하면 total_price를 표시
  if (["S", "M", "L", "Free"].includes(selectedSize)) {
    totalPriceDiv.style.display = "block";
  } else {
    totalPriceDiv.style.display = "none";
  }
}



// thumb이미지주소마다 클릭하면 크기조절불가능한 새창팝업
// thumb과 prodDetailreview가 추가될때마다 수정해줘야함
document.addEventListener('DOMContentLoaded', function () {
  var thumbs = document.querySelectorAll('.review_thumb1 img, .review_thumb2 img, .review_thumb3 img');

  thumbs.forEach(function (thumb) {
    var imageUrl = thumb.getAttribute('src');

    if (imageUrl.includes('https://media.wconcept.co.kr/review/301740098/301740098_1682047290668.jpeg?RS=300')) {
      thumb.addEventListener('click', function () {
        openPopup('/2pro/jongw/리뷰팝업페이지/prodDetailreview2.html');
      });
    } else if (imageUrl.includes('https://media.wconcept.co.kr/review/301631515/301631515_1708653362071.jpg?RS=300')) {
      thumb.addEventListener('click', function () {
        openPopup('/2pro/jongw/리뷰팝업페이지/prodDetailreview.html');
      });
    } else if (imageUrl.includes('image3.jpg')) {
      thumb.addEventListener('click', function () {
        openPopup('../project/prodDetailreview3.html');
      });
    }
  });

  function openPopup(url) {
    var width = 1030;
    var height = 700;
    var left = (window.innerWidth - width) / 2 - 100;
    var top = (window.innerHeight - height) / 2;

    var newWindow = window.open(url, "windowName", "resizable, width=" + width + ", height=" + height + ", left=" + left + ", top=" + top);

    newWindow.addEventListener('DOMContentLoaded', function () {
      var link = newWindow.document.createElement('link');
      link.rel = 'stylesheet';
      link.type = 'text/css';
      link.href = '/2pro/jongw/리뷰팝업페이지/prodDetailreview.css';
      newWindow.document.head.appendChild(link);
    });
  }
});

// q&a 질문을 클릭하면 숨겨진 a가 나타남
document.addEventListener("DOMContentLoaded", function () {
  var questionLinks = document.querySelectorAll(".questionp");

  questionLinks.forEach(function (link) {
    link.addEventListener("click", function (event) {
      event.preventDefault();

      var detail = this.parentElement.parentElement.nextElementSibling;

      if (detail && detail.classList.contains("question_detail")) {
        if (detail.style.display === "none" || detail.style.display === "") {
          detail.style.display = "table-row";
        } else {
          detail.style.display = "none";
        }
      }
    });
  });
});

// 수량증감
document.addEventListener("DOMContentLoaded", function () {
  var priceElement = document.getElementById("total2").querySelector("span");
  var price = parseInt(priceElement.textContent.replace(/\D/g, ""));
  priceElement.dataset.price = price;

  var quantityInput = document.getElementById("quantity");

  // 수량증감 버튼에 이벤트 리스너 등록
  document.getElementById("increaseIcon").addEventListener("click", function () {
    increaseQuantity();
  });

  document.getElementById("decreaseIcon").addEventListener("click", function () {
    decreaseQuantity();
  });

  // 수량을 증가하는 함수
  function increaseQuantity() {
    var currentQuantity = parseInt(quantityInput.value);
    quantityInput.value = currentQuantity + 1;
    calculateTotal();
  }

  // 수량을 감소하는 함수
  function decreaseQuantity() {
    var currentQuantity = parseInt(quantityInput.value);
    if (currentQuantity > 1) {
      quantityInput.value = currentQuantity - 1;
      calculateTotal();
    }
  }

  // 총 금액을 계산하는 함수
  function calculateTotal() {
    var priceElement = document.getElementById("total2").querySelector("span");
    var price = parseInt(priceElement.dataset.price);
    var quantity = parseInt(quantityInput.value);
    var total = price * quantity;
    var formattedTotal = total.toLocaleString();
    priceElement.textContent = formattedTotal;
  }
});


/*상품금액계산*/
    $(document).ready(function(){
            calculateTotalPrice();

            $("#count").change(function(){
                calculateTotalPrice();
            });
        });

        function calculateTotalPrice(){
            const count = $("#count").val();
            const price = $("#price").val();
            const totalPrice = price * count;
            $("#totalPrice").html(totalPrice + "원");
        }


/*주문하기*/
 function order(){
        <!--csrf토큰 값을 가져옴-->
            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");
            <!--ajax비동기통신-->
            const url = "/order";
            const paramData = {
                itemId: $("#itemId").val(),
                count: $("#count").val()
            };
            const param = JSON.stringify(paramData);

            $.ajax({
                url : url,
                type : "POST",
                contentType : "application/json",
                data : param,
                beforeSend : function(xhr){
                    <!--데이터를 전송하기 전에 헤더에 csrf 값을 생성해줘야함-->
                    xhr.setRequestHeader(header, token);
                },
                dataType : "json",
                cache : false,
                success : function(result, status){
                    alert("주문이 완료되었습니다.");
//                    location.href='/';
                },
                error : function(jqXHR, status, error){
                    if(jqXHR.status == '401'){
                        alert("로그인 후 이용해 주세요.");
                        location.href='/member/login';
                    } else {
                        <!--그이외에는 해당 메세지를 출력-->
                        alert(jqXHR.responseText);
                    }
                }
            })
        }

        <!--주문하기버튼누르면 주문되는 이벤트 처리하기-->
         function order() {
             const token = $("meta[name='_csrf']").attr("content");
             const header = $("meta[name='_csrf_header']").attr("content");
             const url = "/order";

             const itemId = $("#itemId").val();
             const count = parseInt($("#quantity").val(), 10); // 수량을 정수형으로 변환
             const size = $("select[name='size']").val();

             // 유효하지 않은 사이즈 검증
             if (!["S", "M", "L", "Free"].includes(size)) {
                 alert("유효하지 않은 사이즈입니다.");
                 return; // 주문 요청 중단
             }

             const paramData = {
                 itemId: itemId,
                 count: count,
                 size: size
             };

             $.ajax({
                 url: url,
                 type: "POST",
                 contentType: "application/json",
                 data: JSON.stringify(paramData), // 데이터를 JSON 형식으로 변환
                 beforeSend: function(xhr) {
                     xhr.setRequestHeader(header, token); // CSRF 헤더 설정
                 },
                 dataType: "json",
                 success: function(result) {
                     alert("주문이 완료되었습니다.");
                     location.href = '/'; // 주문 성공 후 리디렉션
                 },
                 error: function(jqXHR) {
                     if (jqXHR.status === 401) { // 인증 오류
                         alert("로그인 후 이용해 주세요.");
                         location.href = '/member/login';
                     } else {
                         alert("주문 실패: " + jqXHR.responseText); // 기타 오류
                     }
                 }
             });
         }

           function addCart() {
               const token = $("meta[name='_csrf']").attr("content");
               const header = $("meta[name='_csrf_header']").attr("content");
               const itemId = $("#itemId").val(); // 상품 ID
               const count = $("#quantity").val(); // 사용자가 선택한 수량
               const selectedSize = $("select[name='size']").val(); // 선택된 사이즈

               const url = "/cart";
               const paramData = JSON.stringify({
                   itemId: itemId,
                   count: count,
                   size: selectedSize
               });

               $.ajax({
                   url: url,
                   type: "POST",
                   contentType: "application/json",
                   data: paramData,
                   beforeSend: function(xhr) {
                       xhr.setRequestHeader(header, token);
                   },
                   success: function(result) {
                       alert("상품을 장바구니에 담았습니다.");
                       location.reload(); // 현재 페이지를 새로고침
                   },
                   error: function(jqXHR) {
                       if (jqXHR.status == 401) {
                           alert("로그인 후 이용해 주세요.");
                           location.href = '/member/login';
                       } else {
                           alert(jqXHR.responseText);
                       }
                   }
               });
           }

                       function addToCart() {
                           var selectedSize = document.querySelector('select[name="size"]').value;
                           var quantity = parseInt(document.getElementById("quantity").value);

                           if (selectedSize === '' || isNaN(quantity) || quantity < 1) {
                               alert("사이즈와 수량을 선택하세요.");
                               return;
                           }

                           // 장바구니에 상품을 담는 로직
                           addCart();
                       }

/* 결제 모달 불러오기 시작 */
$(document).ready(function() {
  // 모달 열기 버튼 클릭 시 모달 열기
$("#openModalButton").click(function() {
event.preventDefault();

var productName = document.querySelector(".prod_name").textContent;

// 사이즈
var selectElement = document.querySelector("select[name='size']");
var selectedIndex = selectElement.selectedIndex;
var selectedValue = selectElement.options[selectedIndex].value;
    if (selectedValue.length === 0) {
        alert("선택된 사이즈가 없습니다.");
        return;
    }

// 갯수
    var quantityInput = document.getElementById("quantity");

var price = document.getElementById("realPrice").textContent;
console.log(price);

document.querySelector(".goods_span").textContent = productName;
document.getElementById("mountt").textContent = quantityInput.value;
document.getElementById("realPriceM").textContent = price;
document.getElementById("totalPriceM").textContent = price*quantityInput.value;


    var csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                var csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                // API 엔드포인트
                var apiUrl = '/payment';

               fetch(apiUrl, {
                   method: 'GET',
                   headers: {
                       'Content-Type': 'application/json',
                       [csrfHeader]: csrfToken
                   }
               })
           .then(response => {
               if (!response.ok) {
                   return;
               }
               return response.json();
           })

               .then(data => {
               console.log(data.message)
                    if(data.message === 'notFound'){
                          alert('기본 주소지를 설정해주세요.');
                          return;
                    }

                   // 받은 데이터에서 member와 address 추출
                   const member = data.member;
                   const address = data.address;

                   // 여기서부터 데이터를 활용하여 필요한 작업을 수행

                   document.getElementById("nameM").innerText  = member.name;
                   document.getElementById("emailM").innerText  = member.email;
                   document.getElementById("postM").innerText  = address.postcode;
                   document.getElementById("jiM").innerText  = address.jibunAddress;
                   document.getElementById("detM").innerText  = address.detailAddress;


 $("#myModal").css("display", "block");
                   // 예를 들어, HTML에 표시하거나 다른 작업을 수행할 수 있습니다.
               })
               .catch(error => {
                   alert('로그인 후 사용 가능합니다.');
                    return;
               });
});

  // 모달 닫기 버튼 클릭 시 모달 닫기
  $("#btnCancel").click(function() {
    $("#myModal").hide();
  });

  // 모달 외부 클릭 시 모달 닫기
  $(document).mouseup(function(e) {
    var modal = $("#myModal");
    if (!modal.is(e.target) && modal.has(e.target).length === 0) {
      modal.css("display", "none");
    }
  });
});
/* 결제 모달 불러오기 끝 */