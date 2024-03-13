<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome BookMall</title>
<link rel="stylesheet" href="resources/css/main.css">
</head>
<body class="d-flex flex-column h-100">
	<main class="flex-shrink-0">
		<jsp:include page="/WEB-INF/views/include/header.jsp"/>
		<!-- Header-->
		<header class="bg-dark py-5"
			style="background-image: url('/resources/assets/img/header.png'); height: 450px;">
			<div class="container px-5">
				<div class="row gx-5 align-items-center justify-content-center">
					<div class="col-lg-8 col-xl-7 col-xxl-6"></div>

				</div>
			</div>
		</header>
		<!-- Features section-->
		<section id="scroll">
			<div class="container px-5">
				<div class="row gx-5 align-items-center">
					<div class="col-lg-6 order-lg-2">
						<div class="p-5">
							<img class="img-fluid rounded-circle"
								src="resources/assets/img/temper.jpg" alt="체온관리 이미지">
						</div>
					</div>
					<div class="col-lg-6 order-lg-1">
						<div class="p-5">
							<h2 class="display-4">체온관리</h2>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Quod aliquid, mollitia odio veniam sit iste esse assumenda amet
								aperiam exercitationem, ea animi blanditiis recusandae! Ratione
								voluptatum molestiae adipisci, beatae obcaecati.</p>
						</div>
					</div>
				</div>
				<section>
					<div class="container px-5">
						<div class="row gx-5 align-items-center">
							<div class="col-lg-6">
								<div class="p-5">
									<img class="img-fluid rounded-circle"
										src="resources/assets/img/heart.jpg" alt="체온관리 이미지">
								</div>
							</div>
							<div class="col-lg-6">
								<div class="p-5">
									<h2 class="display-4">심박수관리</h2>
									<p>Lorem ipsum dolor sit amet, consectetur adipisicing
										elit. Quod aliquid, mollitia odio veniam sit iste esse
										assumenda amet aperiam exercitationem, ea animi blanditiis
										recusandae! Ratione voluptatum molestiae adipisci, beatae
										obcaecati.</p>
								</div>
							</div>
						</div>
					</div>
				</section>
				<div class="row gx-5 align-items-center">
					<div class="col-lg-6 order-lg-2">
						<div class="p-5">
							<img class="img-fluid rounded-circle"
								src="resources/assets/img/fall.jpg" alt="낙상감지 이미지">
						</div>
					</div>
					<div class="col-lg-6 order-lg-1">
						<div class="p-5">
							<h2 class="display-4">낙상감지</h2>
							<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
								Quod aliquid, mollitia odio veniam sit iste esse assumenda amet
								aperiam exercitationem, ea animi blanditiis recusandae! Ratione
								voluptatum molestiae adipisci, beatae obcaecati.</p>
						</div>
					</div>
				</div>
			</div>
		</section>
	</main>
	<!-- Footer-->
	<footer class="bg-dark py-4 mt-auto">
		<div class="container px-5">
			<div class="row align-items-center justify-content-center">
				<!-- 텍스트를 중앙으로 정렬하기 위해 justify-content-center 추가 -->
				<div class="col-auto">
					<!-- 글씨 크기 키우기 -->
					<div class="small m-0 text-white" style="font-size: 18px;">
						<p class="text-center">Copyright © 2024 A조 최종프로젝트</p>
						<!-- 텍스트를 가운데로 정렬하기 위해 text-center 추가 -->
					</div>
				</div>
			</div>
		</div>
	</footer>
</body>
</html>