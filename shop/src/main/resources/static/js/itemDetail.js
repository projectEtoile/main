// 작은섬네일을 선택하면 큰사진이 변경

var bigPic = document.getElementById("big_img");
var smallPics = document.querySelectorAll(".small");

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
// 사이즈를선택하면 총금액박스가 나타남
function showTotalPrice() {
  var selectBox = document.getElementsByName("size")[0];
  var totalPriceDiv = document.getElementById("total_price");

  var selectedSize = selectBox.value;

  if (selectedSize === "size1" || selectedSize === "size2") {
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
function calculateTotal() {
  var priceElement = document.getElementById('total2').querySelector('span');
  var price = parseInt(priceElement.dataset.price);
  var quantity = document.getElementById('quantity').value;
  var total = price * quantity;
  var formattedTotal = total.toLocaleString();
  priceElement.textContent = formattedTotal;
}

function increaseQuantity() {
  var quantityInput = document.getElementById('quantity');
  var currentQuantity = parseInt(quantityInput.value);
  quantityInput.value = currentQuantity + 1;
  calculateTotal();
}

function decreaseQuantity() {
  var quantityInput = document.getElementById('quantity');
  var currentQuantity = parseInt(quantityInput.value);
  if (currentQuantity > 1) {
    quantityInput.value = currentQuantity - 1;
    calculateTotal();
  }
}

document.getElementById('increaseIcon').addEventListener('click', function () {
  increaseQuantity();
});

document.getElementById('decreaseIcon').addEventListener('click', function () {
  decreaseQuantity();
});


document.addEventListener('DOMContentLoaded', function () {
  var priceElement = document.getElementById('total2').querySelector('span');
  var price = parseInt(priceElement.textContent.replace(/\D/g, ''));
  priceElement.dataset.price = price;
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
                    location.href='/';
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
              const count = $("#count").val();
              const size = $("select[name='size']").val();

              // 유효하지 않은 사이즈 검증
              if (size !== "S" && size !== "M" && size !== "L" && size !== "Free") {
                  alert("유효하지 않은 사이즈입니다.");
                  return; // 주문 요청 중단
              }

              const paramData = {
                  itemId: itemId,
                  count: count,
                  size: size
              };
              const param = JSON.stringify(paramData);

              $.ajax({
                  url: url,
                  type: "POST",
                  contentType: "application/json",
                  data: param,
                  beforeSend: function (xhr) {
                      xhr.setRequestHeader(header, token);
                  },
                  dataType: "json",
                  cache: false,
                  success: function (result, status) {
                      alert("주문이 완료되었습니다.");
                      location.href = '/';
                  },
                  error: function (jqXHR, status, error) {
                      if (jqXHR.status == '401') {
                          alert("로그인 후 이용해 주세요.");
                          location.href = '/member/login';
                      } else {
                          alert(jqXHR.responseText);
                      }
                  }
              });
          }

                <!--장바구니담기 이벤트 처리하기 -->
                  function addCart(){
                <!--csrf토큰 값을 가져옴-->
                    const token = $("meta[name='_csrf']").attr("content");
                    const header = $("meta[name='_csrf_header']").attr("content");
                    <!--ajax비동기통신-->
                    const url = "/cart";
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
                            alert("상품을 장바구니에 담았습니다.");
                            location.href='/';
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
