$(document).ready(function() {
    // 첫 번째 차트
    var ctx1 = $("#myChart1");

    var ageTotal10 = document.getElementById('ageTotal10').textContent;
    var ageTotal20 = document.getElementById('ageTotal20').textContent;
    var ageTotal30 = document.getElementById('ageTotal30').textContent;
    var ageTotal40 = document.getElementById('ageTotal40').textContent;
    var ageTotal50 = document.getElementById('ageTotal50').textContent;
    var ageTotal60 = document.getElementById('ageTotal60').textContent;
    var ageTotal70 = document.getElementById('ageTotal70').textContent;

    var myChart1 = new Chart(ctx1, {
        type: 'bar',
        data: {
            labels: ['10대', '20대', '30대', '40대', '50대', '60대', '70대'],
            datasets: [{
                label: '',
                data: [ageTotal10, ageTotal20, ageTotal30, ageTotal40, ageTotal50, ageTotal60, ageTotal70],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });

    // 두 번째 차트
    var ctx2 = $("#myChart2");

    var male = document.getElementById('male').textContent;
    var female = document.getElementById('female').textContent;

    var myChart2 = new Chart(ctx2, {
        type: 'doughnut',
        data: {
            labels: ['MALE', 'FEMALE'],
            datasets: [{
                label: '매출',
                data: [male, female],
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });

    var ctx3 = $("#myChart3");

        var january = document.getElementById('january').textContent;
        var february = document.getElementById('february').textContent;
        var march = document.getElementById('march').textContent;
        var april = document.getElementById('april').textContent;
        var may = document.getElementById('may').textContent;
        var june = document.getElementById('june').textContent;
        var july = document.getElementById('july').textContent;
        var august = document.getElementById('august').textContent;
        var september = document.getElementById('september').textContent;
        var october = document.getElementById('october').textContent;
        var november = document.getElementById('november').textContent;
        var december = document.getElementById('december').textContent;

        var myChart1 = new Chart(ctx3, {
            type: 'bar',
            data: {
                labels: ['1월', '2월', '3월', '4월', '5월', '6월', '7월','8월','9월' , '10월', '11월', '12월'],
                datasets: [{
                    label: '',
                    data: [january, february, march, april, may, june, july, august,september, october,november, december],
                    backgroundColor: [
                          'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)',
                            'rgba(255, 99, 132, 0.4)',
                            'rgba(54, 162, 235, 0.4)',
                            'rgba(255, 206, 86, 0.4)',
                            'rgba(75, 192, 192, 0.4)',
                            'rgba(153, 102, 255, 0.4)',
                            'rgba(255, 159, 64, 0.4)'
                    ],
                    borderColor: [
                          'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)',
                            'rgba(255, 159, 64, 0.2)',
                            'rgba(255, 99, 132, 0.4)',
                            'rgba(54, 162, 235, 0.4)',
                            'rgba(255, 206, 86, 0.4)',
                            'rgba(75, 192, 192, 0.4)',
                            'rgba(153, 102, 255, 0.4)',
                            'rgba(255, 159, 64, 0.4)'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });
});
