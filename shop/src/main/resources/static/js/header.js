
function itemsPage(element){
var level = element.textContent;
 var searchBy = "";
    var searchQuery = "";
var url = "/items?level="
+ encodeURIComponent(level)
+ "&searchBy=" + encodeURIComponent(searchBy)
+ "&searchQuery=" + encodeURIComponent(searchQuery)
+ "&saleItems=";
  window.location.href = url;
}

function navigateTo(url) {
  window.location.href = url; // 새로운 페이지로 이동
}

/* 헤더 검색 창 기능 시작*/

function itemNmSearch(){
   var searchQ = document.getElementById("inp").value;
var url = "/items?level=&saleItems=&searchBy=itemNm&searchQuery=" + searchQ;
   window.location.href = url;
}





/* 헤더 검색 창 기능 끝*/

document.addEventListener('DOMContentLoaded', function() {
  var categoryBtns = document.querySelectorAll('.dropdown button'); // 일반 드롭다운

  categoryBtns.forEach(function(btn) { // 일반 드롭다운
    var dropdownMenu = btn.nextElementSibling; // 일반 드롭다운

    btn.addEventListener('mouseover', function() {
      dropdownMenu.style.display = 'block';
    });

    dropdownMenu.addEventListener('mouseover', function() {
      dropdownMenu.style.display = 'block';
    });

    btn.addEventListener('mouseout', function() {
      dropdownMenu.style.display = 'none';
    });

    dropdownMenu.addEventListener('mouseout', function() {
      dropdownMenu.style.display = 'none';
    });
  });

  var subDropdownMenus = document.querySelectorAll('.sub-dropdown-menu'); // 2중 드롭다운

  subDropdownMenus.forEach(function(subDropdownMenu) { // 2중 드롭다운
    var parentLi = subDropdownMenu.parentElement; // 2중 드롭다운

    parentLi.addEventListener('mouseover', function() {
      subDropdownMenu.style.display = 'block';
    });

    parentLi.addEventListener('mouseout', function() {
      subDropdownMenu.style.display = 'none';
    });

    subDropdownMenu.addEventListener('mouseover', function() {
      subDropdownMenu.style.display = 'block';
    });

    subDropdownMenu.addEventListener('mouseout', function() {
      subDropdownMenu.style.display = 'none';
    });
  });
});

// // 해당 메뉴에 마우스를 올리면 하위 메뉴를 보여주는 함수
// function showSubMenu(menuId) {
//   const menu = document.getElementById(menuId);
//   const submenu = menu.querySelector('.submenu');
//   submenu.style.display = 'block';
// }

// // 해당 메뉴에서 마우스를 떼면 하위 메뉴를 감추는 함수
// function hideSubMenu(menuId) {
//   const menu = document.getElementById(menuId);
//   const submenu = menu.querySelector('.submenu');
//   submenu.style.display = 'none';
// }

// // 각 메뉴에 이벤트 리스너 등록하는 함수
// function addMenuEventListeners(menuId) {
//   const menu = document.getElementById(menuId);
//   const submenu = menu.querySelector('.submenu');
//   // 초기에는 드롭다운을 닫은 상태로 설정
//   submenu.style.display = 'none';
//   menu.addEventListener('mouseenter', function() { // 마우스를 올렸을 때 이벤트로 변경
//     showSubMenu(menuId);
//   });
//   menu.addEventListener('mouseleave', function() { // 마우스를 내렸을 때 이벤트로 변경
//     hideSubMenu(menuId);
//   });
// }

// // 각 메뉴에 이벤트 리스너 등록
// addMenuEventListeners('header_menu1');
// addMenuEventListeners('header_menu2');
// addMenuEventListeners('header_menu3');
// // 헤더 메뉴3 설정 --------------------------------------------------

// // 헤더 메뉴4 설정 --------------------------------------------------

// // 헤더 메뉴4 설정 --------------------------------------------------


// 슬라이드쇼 시작
let slideIndex = 1;

function initializeSlideShow() {
  showSlides(slideIndex);
  startSlideShow();
}

function plusSlides(n) {
  showSlides(slideIndex += n);
}

// Thumbnail image controls
function currentSlide(n) {
  showSlides(slideIndex = n);
}

// Automatic slideshow
let slideInterval;

function startSlideShow() {
  slideInterval = setInterval(function() {
    plusSlides(1);
  }, 3000);
}

function stopSlideShow() {
  clearInterval(slideInterval);
}

// Show slides
function showSlides(n) {
  let i;
  let slides = document.getElementsByClassName("mySlides");
  let dots = document.getElementsByClassName("dot");
  if (n > slides.length) {slideIndex = 1}
  if (n < 1) {slideIndex = slides.length}
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  if (slides.length > 0) {
    slides[slideIndex-1].style.display = "block";
    dots[slideIndex-1].className += " active";
  }
}


document.addEventListener("DOMContentLoaded", initializeSlideShow);

// 슬라이드쇼 끝

// 검색아이콘 클릭 시작
document.addEventListener('DOMContentLoaded', function() {
  const searchIcon = document.getElementsByClassName('searchIcon123')[0];
  const searchInput = document.getElementById('searchInput');

  if (searchIcon) {
    searchIcon.addEventListener('click', function(event) {
      event.stopPropagation(); // 이벤트 버블링을 막습니다.
      // 검색 입력창을 표시하고 아이콘을 숨깁니다.
      searchInput.style.display = 'block';
      searchIcon.style.display = 'none';
      searchInput.focus(); // 입력창에 포커스를 맞춥니다.
    });
  }

  // document 클릭 시 검색창이 열려있으면 닫고 아이콘을 다시 표시합니다.
  document.addEventListener('click', function(event) {
    if (searchInput.style.display === 'block') {
      searchInput.style.display = 'none';
      searchIcon.style.display = 'block'; // 아이콘을 다시 보이게 합니다.
    }
  });

  // 검색창 자체를 클릭했을 때는 닫히지 않도록 합니다.
  searchInput.addEventListener('click', function(event) {
    event.stopPropagation(); // 검색창 클릭 시 document의 클릭 이벤트가 발생하지 않도록 막습니다.
  });
});