
       // 라벨 값도 넘겨주기
           $(document).ready(function() {
             $('#categorys').change(function() {
               var selectedOptionGroupLabel = $(this).find(':selected').parent().attr('label');
               $('#selectedGroupLabel').val(selectedOptionGroupLabel);
             });
           });


     function PreviewImage(input) {
    var preview = new FileReader();
    var img = input.previousElementSibling; // 해당 input 이전에 있는 img 요소 선택

    preview.onload = function (e) {
        img.src = e.target.result;
        img.style.width = "250px"; // 원하는 너비 설정
        img.style.height = "auto"; // 너비에 맞게 자동으로 높이 조절
    };

    preview.readAsDataURL(input.files[0]);
};