/**
 * jquery.number-run.js
 * jquery 数字滚动插件s
 *
 * By postbird - http://www.ptbird.cn
 * license:MIT
 *
 * 注明：
 * - 该插件是之前网上找到的一个，只支持js参数传入，不支持小数显示，不支持回调.
 * - 目前的版本是我修改过的，如果原作者看到此插件，可以联系我，增加原始插件地址
 */
(function ($) {
    $.fn.numberRun = function (options) {
        var defaultOptions = {
            speed: 24, // 速度
            number: 100, // 需要滚动到的数字
            attr: false, // 是否从attr中获取相关参数
            callback: function () {} // 回调函数
        };
        var options = $.extend({}, defaultOptions, options);
        var domObj = this;
        if (options.attr) {
            options.speed = parseInt(domObj.attr("speed"));
            options.number = domObj.attr("number");
        }
        var baseNum = 100,
            number = options.number,
            speed = Math.floor(number / baseNum),
            sum = 0,
            step = 1,
            int_speed = parseInt(options.speed);
        var timer = setInterval(function () {
            if (step <= baseNum && speed != 0) {
                domObj.text(sum = speed * step);
                step++;
            } else if (sum < number) {
                domObj.text(++sum);
            } else {
                clearInterval(timer);
                // 最终的结果确保小数的情况
                domObj.text(options.number);
                // 执行一次 callback
                options.callback();
            }
        }, int_speed);

    }

})(jQuery);