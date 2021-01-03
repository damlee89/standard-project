<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

    <!DOCTYPE html>
<html lang="ko">
<head>
  <title>ProductSample</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <script src="https://use.fontawesome.com/releases/v5.2.0/js/all.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../css/reset.css">
    <link rel="stylesheet" href="../css/style.css">
    <link rel="stylesheet" media="(max-width:1024px)" href="../css/main_medium.css">
    <link rel="stylesheet" media="(max-width:768px)" href="../css/main_small.css">

    <script defer src="../js/main.js"></script>
</head>


<!-- HEADER -->
<header class="section">
    <div class="inner clearfix">
        <div class="menu-group float--left">
            <!-- <div class="logo"> -->
            <a href="../views/index.html" class="logo"><img src="../img/logo2.png" width="60" height="40"></a>
            <!-- </div> -->
            <ul class="main-menu toggle">
                <li><a href="../views/StoreMain.html">Store</a></li>
                <li><a href="../views/MagazinePages/Magazine.html">Magazine</a></li>
                <li><a href="../views/login_form.html">My page</a></li>
            </ul>
        </div>
        <div class="sign-group float--right toggle">
            <ul class="sub-menu">
                <li><a href="../views/login_form.html">Login</a></li>
                <li><a href="">Register</a></li>
            </ul>
            <a href="#">
                <input type="text">
                <img src="../img/search.svg" alt="search" height="20" width="20">
            </a>
            <a href="#">
                <img src="../img/cart.svg" alt="cart" height="25" width="25">
            </a>
        </div>
        <div id="toggle-btn">Header Menu Toggle Button</div>
    </div>
</header>
 <!-- CONTENT :사이드바 필요하면 넣기 (default:Mypage) -->
<body class="">
    <div class="content_wrap inner">
        <div class="col-auto p-0">
            <div class="side_nav">
                <div class="side_nav_item">
                    <p class="side_nav_title">Shop</p>
                    <ul>
                        <li><a href="#">Men`s</a></li>
                        <li><a href="#"></a></li>
                        <li><a href="#">Cart</a></li>
                        <li><a href="../views/order.html">Order</a></li>
                        <li><a href="#">Wish list</a></li>
                        <li><a href="https://www.cjlogistics.com/ko/tool/parcel/tracking">Delivery</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="container">
                <div class="row align-items-center">
                    <div class="ml-auto">
                        <button class="btn btn-white" src>
                            <a href="../views/MagazinePages/Magazine.html" class="text-secondary">다른 상품 보기</a>
                        </button>
                    </div>
                    <div class="ml-auto">
                        <button class="btn btn-white" src>
                            <a href="../views/MagazinePages/Magazine.html" class="text-secondary">매거진 보러 가기</a>
                        </button>
                    </div>
                </div>
                <div class="row align-items-center">
                    <div class="col-md-4">
                        <img style="min-width:200px;height:200px" class="img-thumbnail" src="../img/Magazine/8.jpg" alt="">
                    </div>
                    <div class="col-md-8">
                        <div class="row p-1">
                            <div class="col-sm-4">ProductInfo</div>
                            <div class="col-sm-8">테스트 ProductInfo</div>
                        </div>
                        <div class="row p-1">
                            <span>
                                <div class="col-sm-4">Price</div>
                                <div class="col-sm-8">535,000 원</div>
                        </span>
                        </div>
                        <div class="row p-1">
                            <div class="col-auto ml-auto">
                                <button class="btn btn-dark" src><a href="*" class="text-white">구매하기</a></button>
                            </div>
                            <div class="col-auto">
                                <button class="btn btn-dark" src><a href="*" class="text-white">장바구니</a></button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
<!-- FOOTER -->
<footer class="section">
    <div class="inner clearfix">
        <ul class="site-links float--right">
            <li><a href="#">API</a></li>
            <li><a href="#">Training</a></li>
            <li><a href="#">Shop</a></li>
            <li><a href="#">About</a></li>
        </ul>
        <ul class="site-links float--left">
            <li>© 2020 STANDARD Inc.</li>
            <li><a href="#">Terms</a></li>
            <li><a href="#">Status</a></li>
            <li><a href="#">Help</a></li>
        </ul>
    </div>
</footer>
</html>


<script>
    $(document).ready(function () {
           var val = location.href.substr(
               location.href.lastIndexOf('=') + 1
           );
           
           
           var step;
             for (step = 1; step < 21; step++) {
               if(step == val)
               {
                 var html = '';
                 var title = "";
                 var contents = "";
                 var img = "../img/Magazine/" + val + ".jpg";
                 
               
 
                   title = "매거진" + step;
 
                   html += '<section class="4u"> <a href="#" class="image featured"><img style="width:100%" class="img-thumbnail" src=' + img + ' alt=""></a>';
                   html += '<div class="box">';
                   html += '<p>' + title + '</p>';
                   html += '<p>' + contents + '</p>';
                 //   html += '<a href="#" class="button">더보기</a> </div>';
                   
                   html += '</section>';
 
                 document.getElementById("test").innerHTML = html;
               }
             }
 
             
       });
 </script>
</body>
</html>