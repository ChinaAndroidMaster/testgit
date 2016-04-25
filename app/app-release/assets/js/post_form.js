$(document)
		.ready(
				function() {
					
					$("#chooseModel").on('click', function(e) {
						$(".touch-screen,.selectpick_div").hide();
						$(".selectpick_options").hide();
						e.preventDefault();
						$("#brand_modal").show();
						return false
					});

					$(".brand_list li")
							.on(
									'click',
									function(e) {
										var that = this;
										window.scrollTo(0, 0);
										window.brandName = that
												.getElementsByTagName('a')[0].innerText;
										// window.brandId =
										// that.getAttribute('data-source');
										// $.cookie('brandId',window.brandId);
										var seriesList = document
												.getElementById('series_list');
										seriesList.innerHTML = "正在加载..";
										$(this).parents('.C_modal').hide();
										$("#series_modal").show();
										$
												.getJSON(
														metaURL
																+ "c2c/series_brand"
																+ that
																		.getAttribute('data-source')
																+ ".json?version="
																+ window.staticVersion,
														function(e) {
															if (e.length > 0) {
																seriesList.innerHTML = "";
																for ( var i in e) {
																	var _li = document
																			.createElement('li');
																	_li
																			.setAttribute(
																					'data-source',
																					e[i].key);
																	_li.innerHTML = '<a>'
																			+ e[i].value
																			+ '</a>';
																	_li
																			.addEventListener(
																					'click',
																					function(
																							e) {
																					})
																	seriesList
																			.appendChild(_li);
																}
															}
														});
										return false
									})

					$(".series_list li")
							.live(
									'click',
									function(e) {
										var that = this;
										window.scrollTo(0, 0);
										window.seriesName = that
												.getElementsByTagName('a')[0].innerText;
										// window.seriesId =
										// that.getAttribute('data-source');
										// $.cookie('seriesId',window.seriesId);
										var model_list = document
												.getElementById('model_list');
										model_list.innerHTML = "正在加载...";
										$(this).parents('.C_modal').hide();
										$("#modelListTitle").html(
												"选车型(" + window.seriesName
														+ ")");
										$("#model_modal").show();
										$
												.getJSON(
														metaURL
																+ "c2c/model_series"
																+ that
																		.getAttribute('data-source')
																+ ".json?version="
																+ window.staticVersion,
														function(e) {
															if (e.length > 0) {
																model_list.innerHTML = "";
																var modelYear = "";
																for ( var i in e) {
																	var year = e[i].year;
																	if (year != modelYear) {
																		var yearLi = document
																				.createElement('li');
																		yearLi
																				.setAttribute(
																						"style",
																						"background: #e1e5ed;border:none;color:#2a6496");
																		yearLi.innerHTML = year
																				+ "款";
																		model_list
																				.appendChild(yearLi);
																		modelYear = year;
																	}
																	var _li = document
																			.createElement('li');
																	_li
																			.setAttribute(
																					'data-source',
																					e[i].key);
																	_li
																			.setAttribute(
																					'data-max',
																					e[i].max_year);
																	_li
																			.setAttribute(
																					'data-min',
																					e[i].min_year);
																	_li
																			.setAttribute(
																					'data-bid',
																					e[i].brand_id);
																	_li
																			.setAttribute(
																					'data-sid',
																					e[i].series_id);
																	_li
																			.setAttribute(
																					'data-name',
																					e[i].nameL);
																	_li.innerHTML = '<span class="detail_model">'
																			+ e[i].value
																			+ '</span><span class="model_price">'
																			+ Number(
																					e[i].price)
																					.toFixed(
																							2)
																			+ '万</span>';
																	_li
																			.addEventListener(
																					'click',
																					function(
																							e) {
																					})
																	model_list
																			.appendChild(_li);
																}
															}
														});
										return false
									})

					$(".model_list li")
							.live(
									'click',
									function(e) {
										var that = this;
										window.scrollTo(0, 0);
										if (!that.hasAttribute("data-name"))
											return;
										window.modelName = that
												.getAttribute('data-name');
										window.brandId = that
												.getAttribute('data-bid');
										window.seriesId = that
												.getAttribute('data-sid');
										window.modelId = that
												.getAttribute('data-source');
										$.cookie('brandId', window.brandId);
										$.cookie('seriesId', window.seriesId);
										$.cookie('modelId', window.modelId);
										$.cookie('modelName', window.modelName);
										var minYear = Number(that
												.getAttribute('data-min'));
										var maxYear = Number(that
												.getAttribute('data-max'));
										var year_select = document
												.getElementById('year');

										$("#year_select")
												.removeAttr('disabled');
										$("#chooseModel").find(".select_value")
												.text(window.modelName);
										$(this).parents('.C_modal').hide();
										$(".touch-screen,.selectpick_div")
												.show();
										$.cookie('minYear', minYear);
										$.cookie('maxYear', maxYear);
										if (window.hasDefaultValue) {
											year_select.innerHTML = "";
											window.yearValue = maxYear;
										} else {
											year_select.innerHTML = "<option value=''>上牌年份</option>";
											window.yearValue = "";
										}
										for (var i = minYear; i <= maxYear; i++) {
											var _option = document
													.createElement('option');
											_option.value = i;
											_option.innerText = i;
											year_select.appendChild(_option);

											$("#selectpick_year_parentdiv")
													.remove();
											$("#year")
													.che300pick(
															{
																width : optionWidth,
																selectText : window.yearValue,
																onSelect : function(
																		value,
																		text) {
																	window.yearValue = value;
																}
															});
										}
										return false
									});

					$(".modal-back").on('click', function(e) {
						e.preventDefault();
						$(this).parents('.C_modal').hide();
						$(this.getAttribute('target')).show();
						return false
					})

					var $brandSelect = $("#brand_id");
					if ($brandSelect.val()) {
						$brandSelect.trigger("change");
					}

					var $provSelect = $("#prov");
					var $citySelect = $("#city");
					if ($provSelect.val() && $citySelect.val() == "") {
						$provSelect.trigger("change");
					}
					$("#evaluate_form")
							.on(
									"submit",
									function(e) {
										var _me = this;
										e.preventDefault();
										var prov = window.prov;
										var city = window.city;
										var sld = window.sld;
										var modelId = window.modelId;
										var year = window.yearValue;

										var month = window.monthValue;
										var mile = $("#mile");
										var mileAge = mile.val()
												/ mile.attr('unit');
										var regDate = year + "-" + month;
										var brandId = window.brandId ? window.brandId
												: $.cookie('brandId');
										var seriesId = window.seriesId ? window.seriesId
												: $.cookie('seriesId');

										var partnerId = $("#partner_id").val();

										if (prov == undefined || prov == "") {
											showErrorTips("请选择省份！");
											return;
										}
										if (city == undefined || city == "") {
											showErrorTips("请选择城市！");
											return;
										}

										if (modelId == undefined
												|| modelId == "") {
											showErrorTips("请选择车型！");
											return;
										}
										if (year == undefined || year == "") {
											showErrorTips("请选择上牌年份！");
											return;
										}
										if (month == undefined || month == "") {
											month = $("#month").val();
											if (month == "") {
												showErrorTips("请选择上牌月份！");
												return;
											}
										}
										if (mileAge == "") {
											showErrorTips("行驶里程不可为空！");
											return;
										} else if (parseFloat(mileAge) <= 0) {
											showErrorTips("请正确填写行驶里程！");
											return;
										} else if (isNaN(parseFloat(mileAge))) {
											showErrorTips("请正确填写行驶里程！");
											return;
										} else if (parseFloat(mileAge) >= 100) {
											if (parseInt(mile.attr('unit')) == 1) {
												showErrorTips("行驶里程单位是万公里！");
											} else {
												showErrorTips("行驶里程过大不合法！");
											}
											return;
										}
										var sellIntent = 0;
										var sell = $("input[name=sell]");
										if (sell.length > 0) {
											sell = $("input[name=sell]:checked");
											if (sell.length == 0) {
												showErrorTips("请选择是否打算卖这辆车！");
												return;
											} else {
												sellIntent = sell[0].value;
											}
										}
										var contact = $("#contact");
										if (contact.length > 0) {
											contact = $.trim(contact.val());
											if (contact == "") {
												showErrorTips("联系人姓名不可为空！");
												return;
											}
										} else {
											contact = "";
										}

										var tel = $("#tel");
										if (tel.length > 0) {
											tel = $.trim(tel.val());
											if (tel == "") {
												showErrorTips("手机号码不可为空！");
												return;
											} else if (!validMobileFormat(tel)) {
												showErrorTips("手机号码不合法！");
												return;
											}
										} else {
											tel = "";
										}
										if ($('#web_version').val() == true) {
											var url = "../CarEvaluate.php?";
										} else {
											//var path = $("#path").val();
											// var url =
											// "http://www.che300.com/partner/result.php?";
											var url = "http://sfkctest.vpluser.com:8050/pinggu/pinggu/shifu_result?";
										//	var url ="http://10.100.62.29:8080/pinggu/shifu_result?";
										}
										$.cookie('evalYear', year);
										$.cookie('evalMonth', month);
										$.cookie('modelId', window.modelId);
										$.cookie('modelName', window.modelName);
										$.cookie('mileAge', mile.val());

										url += "prov=" + prov + "&city=" + city
												+ "&brand=" + brandId
												+ "&series=" + seriesId
												+ "&model=" + modelId
												+ "&registerDate=" + regDate
												+ "&mileAge="
												+ parseFloat(mileAge)
												+ "&intention=" + sellIntent
												+ "&partnerId=" + partnerId;
										if (tel != "") {
											url += "&tel=" + tel;
										}
										if (contact != "") {
											url += "&contact=" + contact;
										}
										if (sld != undefined && sld != "") {
											/*
											 * $.cookie('sld', sld, {domain:
											 * 'che300.com'});
											 */
											$.cookie('sld', sld);
										}

										location.href = url;
								
									});

				});

function disappearErrorMsg() {
	window.setTimeout(function() {
		$("#errorMsg").css("display", "none");
	}, 3000);
}

function showErrorTips(tip) {
	$("#errorMsg").html(tip);
	$("#errorMsg").css("display", "");
	// disappearErrorMsg();
}

function validMobileFormat(mobile) {
	var numberRegStr = /^\d{11}$/;
	var regNum = new RegExp(numberRegStr);
	return regNum.test(mobile);
}
