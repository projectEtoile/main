//    $(document).ready(function() {
//        $("#toggleButtonC").click(function() {
//            // 아래 히든 태그를 보여줍니다.
//            event.preventDefault();
//            $("#elementToToggle2").removeClass("hidden");
//        });
//
//        // 취소하기 버튼에 클릭 이벤트를 추가합니다.
//        $("#toggleButtonD").click(function() {
//        function changePw(){
//            // 아래 히든 태그를 숨깁니다.
//            $("#elementToToggle2").addClass("hidden");
//        });
//    });

    function viewShow(event){
        event.preventDefault();
        $("#elementToToggle2").removeClass("hidden");
    }
    function viewHidden(event){
    event.preventDefault();
                // 아래 히든 태그를 숨깁니다.
                $("#elementToToggle2").addClass("hidden");
            }


    function changePw(event){
event.preventDefault();
    const newPw1 = $("#newPw1").val();
    const newPw2 = $("#newPw2").val();

    if(newPw1.length < 8 || newPw1.length > 16 || newPw2.length < 8 || newPw2.length > 16){
        alert("비밀번호의 길이는 8 이상 16 이하로 입력해주세요");
        console.log(newPw1);
    }
   else if(newPw1 !== newPw2){
        alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다")

   }else if(newPw1 == newPw2){


            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");

            const url = "/mypage/changePw";
            const paramData = {
                newPw1: newPw1
            };
            const param = JSON.stringify(paramData);

            $.ajax({
                url : url,
                type : "PATCH",
                contentType : "application/json",
                data : param,
                beforeSend : function(xhr){
                    // 데이터를 전송하기 전에 헤더에 csrf 값을 설정
                    xhr.setRequestHeader(header, token);
                },
                dataType : "json",
                cache : false,
                success : function(result, status){
                    alert(result);
                },
                error : function(jqXHR, status, error){
                    if(jqXHR.status == '401'){
                        alert("로그인 후 이용해 주세요.");
                        location.href='/member/login';
                    } else {
                        // 그 외의 경우 해당 메세지를 출력
                        alert(jqXHR.responseText);
                    }
                }
            })


    }
}



    function updateInfo(event){

event.preventDefault();

    const name = $("#name").val();
    const age = $("#age").val();
    const sex = $("#sex").val();


            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");

            const url = "/mypage/updateInfo";
            const paramData = {
                name: name,
                age: age,
                sex: sex
            };
            const param = JSON.stringify(paramData);

            $.ajax({
                url : url,
                type : "PUT",
                contentType : "application/json",
                data : param,
                beforeSend : function(xhr){
                    // 데이터를 전송하기 전에 헤더에 csrf 값을 설정
                    xhr.setRequestHeader(header, token);
                },
                dataType : "json",
                cache : false,
                success : function(result, status){
                    alert(result);
                },
                error : function(jqXHR, status, error){
                    if(jqXHR.status == '401'){
                        alert("로그인 후 이용해 주세요.");
                        location.href='/member/login';
                    } else {
                        // 그 외의 경우 해당 메세지를 출력
                        alert(jqXHR.responseText);
                    }
                }
            })


    }


