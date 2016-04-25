(function($, window, document, undefined) {

    $.fn.che300pick = function(options) {
        // selectpick的配置
        var selectpick_config = {
            height: 36,
            optionHeight:200,
            width: 150,
            optionColor: "#e1e5ed",
            selectedColor:"#e1e5ed",
            disabled: false, // 是否禁用,默认false
            selectText: "", // 设置哪个文本被选中
            onSelect: "", // 点击后选中事件
            jsonData:null
        };

        var settings = $.extend({}, selectpick_config, options);
        // 每个下拉框组件的操作
        return this.each(function(elem_id) {
            var obj = this;
            var _offset = $(this).offset();
            var top = _offset.top;
            var elem_width = $(obj).width();
            var left = _offset.left;
            var elem_id = $(obj).attr("id"); // 元素的ID
            // 生成的div的样式
            var _selectBody = "<div id='selectpick_"+elem_id+"_parentdiv' onselectstart='return false;'>" +
                "<div class='selectpick_div selectpick_div_" + elem_id + "'  id='selectpick_" + elem_id + "'><span style='float:left;' id='selectpick_span_" + elem_id + "'></span><span class='selectpick_icon' id='selectpick_icon_" + elem_id + "'></span></div>" +
                "<div class='selectpick_options selectpick_options_" + elem_id + "'></div></div>";
            $(_selectBody).appendTo("body");
            $(obj).addClass("select_hide");

            // 设置selectpick显示的位置
            $(".selectpick_div_" + elem_id).css({
                "height": settings.height,
                "width": settings.width,
                "left": left,
                "top": top
            });

            // 设置默认显示在div上的值
            if (settings.selectText != "" && settings.selectText != undefined) {
                $(".selectpick_div_" + elem_id + " span").first().text(settings.selectText);
            } else {
                $(".selectpick_div_" + elem_id + " span").first().text($(obj).children("option").first().text());
            }

            // 是否禁用下拉框
            if(settings.disabled){
                $(".selectpick_div_" + elem_id).addClass("selectpick_no_select");
                $("#selectpick_icon_" + elem_id).css({"cursor":"default"});
                return;
            }
            // 点击div显示列表
            $(".selectpick_div_" + elem_id + ",#selectpick_span_" + elem_id + ",#selectpick_options_" + elem_id + "").bind("click", function(event) {
                var selected_text = $(".selectpick_div_" + elem_id + " span").first().text(); // 当前div中的值
                event.stopPropagation(); //  阻止事件冒泡

                if ($(".selectpick_ul_" + elem_id + " li").length > 0) {
                    // 隐藏和显示div
                    $(".selectpick_options_" + elem_id).empty().css({"border-top":"none"});
                    return;
                } else {
                    $(".selectpick_options_" + elem_id).css({"border-top":"solid 1px #CFCFCF"});
                    $(".selectpick_options ul li").remove();
                    // 添加列表项
                    var ul = "<ul class='selectpick_ul_" + elem_id + "'>";
                    var actualOptionHeight;
                    if(settings.jsonData){
                        var _jsonData = settings.jsonData,_jsonIndex = 0;
                        for(var i in _jsonData){
                            if(i==0){
                                ul += "<li class='selectpick_options_selected' style='width:"+settings.width+";font-size:13px;background-color:" + settings.selectedColor + ";color:#000;height:" + (settings.height - 3) + "px; line-height:" + (settings.height - 3) + "px;font-size:13px;'><label style='display:none;'>" + _jsonData[i].key + "</label><label>" + _jsonData[i].value + "</label></li>";
                            }else{
                                ul += "<li style='width:"+settings.width+";font-size:13px;height:" + (settings.height - 3) + "px; line-height:" + (settings.height - 3) + "px;'><label style='display:none;'>" + _jsonData[i].key + "</label><label>" + _jsonData[i].value + "</label></li>";
                            }
                            _jsonIndex ++;
                        }
                        actualOptionHeight = _jsonIndex*settings.height;
                    }else{
                        $(obj).children("option").each(function() {
                            if ($(this).text() == selected_text) {
                                
                                ul += "<li class='selectpick_options_selected' style='width:"+settings.width+";font-size:13px;background-color:" + settings.selectedColor + ";color:#000;height:" + (settings.height - 3) + "px; line-height:" + (settings.height - 3) + "px;font-size:13px;'><label style='display:none;'>" + $(this).val() + "</label><label>" + $(this).text() + "</label></li>";
                            } else {
                                ul += "<li style='width:"+settings.width+";font-size:13px;height:" + (settings.height - 3) + "px; line-height:" + (settings.height - 3) + "px;'><label style='display:none;'>" + $(this).val() + "</label><label>" + $(this).text() + "</label></li>";
                            }
                        });
                        actualOptionHeight = $(obj).children("option").length * settings.height;
                    }
                    ul += "</ul>";
                    $(".selectpick_options_" + elem_id).css({
                        "width": settings.width + 5,
                        "left": left,
                        "height":settings.optionHeight>actualOptionHeight?actualOptionHeight:settings.optionHeight,
                        "overflow":'auto',
                        "top": top + settings.height
                    }).append(ul).show();

                    // li鼠标滑过事件
                    $(".selectpick_options_" + elem_id + " ul li").hover(function() {
                        $(this).css({
                            "background-color": settings.optionColor,
                            "color": "#fff"
                        });
                    }, function() {
                        if ($(this).hasClass("selectpick_options_selected")) {
                            $(this).css({
                                "background-color": settings.optionColor,
                                "color": "#fff"
                            });
                        } else {
                            $(this).css({
                                "background-color": "",
                                "color": "#000"
                            });
                        }

                    });

                    // 每个li点击事件
                    $(".selectpick_ul_" + elem_id + " li").bind("click", function() {
                        $(".selectpick_div_" + elem_id + " span").first().text($(this).children("label").first().next().text());
                        $(".selectpick_options_" + elem_id).empty().hide();
                        // 回调函数
                        if (settings.onSelect != undefined && settings.onSelect != "" && typeof settings.onSelect == "function") {
                            settings.onSelect($(this).children("label").first().text(), $(this).children("label").first().next().text());
                        }
                    });
                }

            });
            // 点击div外面关闭列表
            $(document).bind("click", function(event) {
                var e = event || window.event;
                var elem = e.srcElement || e.target;
                if (elem.id == "selectpick_" + elem_id || elem.id == "selectpick_icon_" + elem_id || elem.id == "selectpick_span_" + elem_id) {
                    return;
                } else {
                    $(".selectpick_options_" + elem_id).empty().hide();
                }
            });

        });
    }
})(jQuery, window, document);