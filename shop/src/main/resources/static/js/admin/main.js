$(document).ready(function() {
    var context1 = $('#myChart1')[0].getContext('2d');
    var myChart1 = new Chart(context1, {
        type: 'bar', // 차트의 형태
        data: { // 차트에 들어갈 데이터
            labels: [
                //x 축
                '8월','9월','10월','11월','12월','1월','2월'
            ],
            datasets: [
                { //데이터
                    label: '매출', //차트 제목
                    fill: false, // line 형태일 때, 선 안쪽을 채우는지 안채우는지
                    data: [
                        2450,3150,2750,3320,3100,2700,1800 //x축 label에 대응되는 데이터 값
                    ],
                    backgroundColor: [
                        //색상
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)',
                        'rgba(255, 159, 64, 0.2)'
                    ],
                    borderColor: [
                        //경계선 색상
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)',
                        'rgba(255, 159, 64, 1)'
                    ],
                    borderWidth: 1 //경계선 굵기
                }/* ,
                {
                    label: 'test2',
                    fill: false,
                    data: [
                        8, 34, 12, 24
                    ],
                    backgroundColor: 'rgb(157, 109, 12)',
                    borderColor: 'rgb(157, 109, 12)'
                } */
            ]
        },
        options: {
            scales: {
                yAxes: [
                    {
                        ticks: {
                            beginAtZero: true
                        }
                    }
                ]
            }
        }
    });

   var OP = parseInt($('#OP').text());
   var DngP = parseInt($('#DngP').text());

   var sum =OP+DngP;

   $('#TTP').text(sum);



    var context2 = $('#myChart2')[0].getContext('2d');

    var maleValue = document.getElementById('male').textContent;
    var femaleValue = document.getElementById('female').textContent;

    var ttp1 = document.getElementById('ttp1').textContent;
    var otp = document.getElementById('otp').textContent;
    var ptp = document.getElementById('ptp').textContent;
    var sdtp = document.getElementById('sdtp').textContent;
    var stp = document.getElementById('stp').textContent;

    var myChart2 = new Chart(context2, {
        type: 'pie', // 차트의 형태
        data: { // 차트에 들어갈 데이터
            labels: [
                //x 축
                'Top','Outer','Pants','Skirt/Dress','Shoes'
            ],
            datasets: [
                { //데이터
                    label: '매출', //차트 제목
                    fill: false, // line 형태일 때, 선 안쪽을 채우는지 안채우는지
                    data: [
                        ttp1,otp,ptp,sdtp,stp
                    ],
                    backgroundColor: [
                        //색상
                        'rgba(255, 99, 132, 0.2)',
                        'rgba(54, 162, 235, 0.2)',
                        'rgba(255, 206, 86, 0.2)',
                        'rgba(75, 192, 192, 0.2)',
                        'rgba(153, 102, 255, 0.2)'
                    ],
                    borderColor: [
                        //경계선 색상
                        'rgba(255, 99, 132, 1)',
                        'rgba(54, 162, 235, 1)',
                        'rgba(255, 206, 86, 1)',
                        'rgba(75, 192, 192, 1)',
                        'rgba(153, 102, 255, 1)'
                    ],
                    borderWidth: 1 //경계선 굵기
                }/* ,
                {
                    label: 'test2',
                    fill: false,
                    data: [
                        8, 34, 12, 24
                    ],
                    backgroundColor: 'rgb(157, 109, 12)',
                    borderColor: 'rgb(157, 109, 12)'
                } */
            ]
        },
        options: {
            scales: {
                yAxes: [
                    {
                        ticks: {
                            beginAtZero: true
                        }
                    }
                ]
            }
        }
    });
});
