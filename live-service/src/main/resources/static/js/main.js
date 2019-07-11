// Hello.
//
// This is The Scripts used for ___________ Theme
//
//

function main() {

    (function () {
        'use strict';

        /* ==============================================
             Testimonial Slider
             =============================================== */

        $('a.page-scroll').click(function () {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') && location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top - 40
                    }, 900);
                    return false;
                }
            }
        });

        /*====================================
        Show Menu on Book
        ======================================*/
        $(window).bind('scroll', function () {
            var navHeight = $(window).height() - 100;
            if ($(window).scrollTop() > navHeight) {
                $('.navbar-default').addClass('on');
            } else {
                $('.navbar-default').removeClass('on');
            }
        });

        $('body').scrollspy({
            target: '.navbar-default',
            offset: 80
        })

        $(document).ready(function () {
            $("#team").owlCarousel({

                navigation: false, // Show next and prev buttons
                slideSpeed: 300,
                paginationSpeed: 400,
                autoHeight: true,
                itemsCustom: [
                    [0, 1],
                    [450, 2],
                    [600, 2],
                    [700, 2],
                    [1000, 4],
                    [1200, 4],
                    [1400, 4],
                    [1600, 4]
                ],
            });

            $("#clients").owlCarousel({

                navigation: false, // Show next and prev buttons
                slideSpeed: 300,
                paginationSpeed: 400,
                autoHeight: true,
                itemsCustom: [
                    [0, 1],
                    [450, 2],
                    [600, 2],
                    [700, 2],
                    [1000, 4],
                    [1200, 5],
                    [1400, 5],
                    [1600, 5]
                ],
            });

            $("#testimonial").owlCarousel({
                navigation: false, // Show next and prev buttons
                slideSpeed: 300,
                paginationSpeed: 400,
                singleItem: true
            });

        });

  	/*====================================
    Portfolio Isotope Filter
    ======================================*/

        var recruitData = {
            'research_staff': [
                {
                    'position_title': '测试工程师',
                    'responsibilities': '参与产品开发和需求讨论，制订测试计划，并与团队成员沟通确保测试能顺利执行并完成；根据产品设计、需求文档等，自行设计和编写测试用例；自行搭建产品测试环境，并完成相关的测试，对测试实施过程中发现的问题进行跟踪分析和反馈，完整地记录测试结果，编写完整的测试报告等相关的技术文档；组织产品的测试实施工作，保障测试的进展和完成，及时沟通解决重大测试问题，确保测试目标的达成；总结测试过程中发现的问题，做好记录、及时反馈，并提出书面分析和改善对策报告。',
                    'requirements': '1年以上产品测试经验；了解并使用相关测试工具，熟悉测试计划、测试用例、测试方案、bug跟踪及测试报告的实施；具备良好的逻辑分析能力，能根据业务需求及项目文档快速理解业务及需求；具备良好沟通、学习能力，有团队合作精神，工作责任心强。',
                    'salary_and_welfare': '6-8千/月。'
                }
            ],
            'operation': [
                {
                    'position_title': '活动运营（线下）',
                    'responsibilities': '协助活动负责人制定项目管理所需的各类管理文件，并做好成本控制；在相关部门的配合下，配合团队成员完成项目的具体实施执行工作；在活动执行过程中，能够做到对时间和质量的严格控制；有效衔接并协调公司内部各部门在项目实施过程中的密切配合；协助活动负责人做好活动各环节的对接、管理及执行；及时的客户联络、准确的信息传递和全面周到的客户服务。',
                    'requirements': '1年以上工作经验，有活动传播相关行业，有大型活动公司经验优先；大专以上学历，传播、广告、会展等相关专业优先；熟练使用office软件；较强的沟通及协调能力，能与客户进行执行性沟通；具有创新和团队合作精神，能够吃苦耐劳。',
                    'salary_and_welfare': '3000底薪+绩效奖金+优秀员工奖金+年终奖；通畅公平的晋升体系：所有管理层均从内部员工提升，为每位员工提供广阔的晋升空间。人性化的福利政策：业绩优秀者享有各种现金奖和国内外旅游等多项福利。公司内定期聚会、野外生存、户外拓展训练假日旅游、生日、带薪年Party假等。五险一金（养老保险、医疗保险、工伤保险、失业保险、商业保险、住房公积金）。'
                },
                {
                    'position_title': '内容审核/客服',
                    'responsibilities': '对用户上传的视频、图片、评论等内容进行审核，及时删除违反国家 法律规定的内容，维护网站的绿色、正常秩序；处理用户对于节目的举报以及其他信息反馈。',
                    'requirements': '有较强的学习能力，熟记相关规则；具有较强的组织计划能力、协调能力及执行能力，善于团队合作；了解重大新闻事件和新闻背景，对时政内容较敏感；男女不限；学历要求：大专及以上',
                    'salary_and_welfare': '3000-4000以上。其他福利待遇：五险一金、带薪年假、岗前培训、绩效奖金、定期体检、交通补贴、餐饮补贴。'
                }
            ],
            'marketer': [
                {
                    'position_title': '市场推广专员BD',
                    'responsibilities': '根据公司市场策略，进行餐饮/酒店行业信息收集，达成业绩目标，获取市场份额；根据公司战略，与餐厅/酒店建立商家合作关系，维护高价值的商户；与公司各部门配合，积极主动及时处理商户的反馈、投诉和建议，提高顾客满意度；有互联网餐饮从业经验优先考虑。',
                    'requirements': '学历：高中及以上，专业不限，喜爱互联网行业，爱折腾的靠谱青年，渴望改变世界；学习能力和适应能力强，能吃苦，渴望快速成长，适应互联网行业的快速发展，具备在高强压力下出色完成任务的 能力；目标导向，具备较强的人际沟通能力及逻辑思维能力，善于换位思考，高效整合内外部资源促成销售业绩；富有激情和创新理念，追求个人职业发展和公司利益的双赢。有餐饮行业销售拓展从业经历，在美团大众、阿里口碑美食或百度糯米网做过拓展销售工作经历的优先考虑。',
                    'salary_and_welfare': '6000+提成+优秀员工奖金，上不封顶，多劳多得；公司内定期聚会、野外生存、户外拓展训练假日旅游、生日、带薪年Party假等；五险一金（养老保险、医疗保险、工伤保险、失业保险、商业保险、住房公积金）；公平公正的晋升模式：销售代表—销售主管—运营总监—分公司经理—董事会通畅公平的晋升体系：所有管理层均从内部员工提升，为每位员工提供广阔的晋升空间。'
                }
            ],
            'HR': [
                {
                    'position_title': 'CEO助理',
                    'responsibilities': '35岁以下女性，擅长商务洽谈具有综合管理能力，气质佳能接受出差；负责协助有关会议的会务安排事宜，记录、整理会议纪要，并根据需要撰写邮件、公文 、报告等各种文件；配合 CEO 处理外部公共关系，协调 CEO 工作日程；负责起草各类文件、演示稿等；负责协调 CEO 商务行程、商务宴请等联络安排工作，并负责对外的客户联系及沟通； 打理CEO的个人事务。',
                    'requirements': '专科及以上学历，3年以上总经理助理或互联网行业管理经验，有融资谈判经验者优先考虑；有较强的组织、协调、沟通、领导、人际交往能力及敏锐的洞察力，逻辑思维能力强、具有很强的判断与决策能力，计划和执行能力；具备较强的文字表述能力，能独立撰写各类文件及报告；工作认真细致，条理性好，具有敬业精神与责任心。',
                    'salary_and_welfare': '12万/年薪以上。'
                },
                {
                    'position_title': '招聘经理',
                    'responsibilities': '负责公司人力资源规划、招聘与配置、培训与开发、薪酬福利管理、劳动者关系管理等工作的全面开展；负责对公司的各项规章制度，管理办法、通知、决议的贯彻执行情况监督和保存；组织、协调公司各类活动及各类重要会议；根据公司经营情况，分析人力资源需求，制定招聘计划、招聘程序，确保人力资源的招聘、使用、管理、开发和人力成本的控制。',
                    'requirements': '大专以上学历，男女不限；有2年以上人力资源管理工作经历；具有良好的道德品质和企业意识；通晓人力资源管理理论，具有人力资源管理、培训实践经验；较强的计划能力和执行力；熟练操作电脑等办公设备，基本的网络知识。',
                    'salary_and_welfare': '10万/年薪以上。'
                }
            ]
        }
        var template = '';
        $.each(recruitData['research_staff'], function (i, e) {
            template += '<p class="position_title">' + (i + 1) + '、' + e['position_title'] + '</p>'
                + '<p>岗位职责：</p>'
                + '<p class="joinUsResponse">' + e['responsibilities'] + '</p>'
                + '<p>岗位要求：</p>'
                + '<p class="joinUsResponse">' + e['requirements'] + '</p>'
                + '<p>薪酬福利：</p>'
                + '<p class="joinUsResponse">' + e['salary_and_welfare'] + '</p>'
        })
        $('#joinUsContent').html(template);
        $('#joinUsUl li').click(function () {
            $('#joinUsUl li').removeClass('active');
            $(this).addClass('active');
            var modalTxt = $(this).attr('name'), template = '';
            $.each(recruitData[modalTxt], function (i, e) {
                template += '<p class="position_title">' + (i + 1) + '、' + e['position_title'] + '</p>'
                    + '<p>岗位职责：</p>'
                    + '<p class="joinUsResponse">' + e['responsibilities'] + '</p>'
                    + '<p>岗位要求：</p>'
                    + '<p class="joinUsResponse">' + e['requirements'] + '</p>'
                    + '<p>薪酬福利：</p>'
                    + '<p class="joinUsResponse">' + e['salary_and_welfare'] + '</p>'
            })
            $('#joinUsContent').html(template);
        })

        $.fn.scrollLoading = function (options) {
            var defaults = {
                attr: "data-url"
            };
            var params = $.extend(
                {},
                defaults,
                options || {}
            );
            params.cache = [];

            $(this).each(function () {
                var node = this.nodeName.toLowerCase(), url = $(this).attr(params["attr"]);
                if (!url) {
                    return;
                }
                var data = {
                    obj: $(this),
                    tag: node,
                    url: url
                };
                params.cache.push(data);
            });

            var loading = function () {
                var st = $(window).scrollTop(), sth = st + $(window).height();
                $.each(params.cache, function (i, data) {
                    var o = data.obj, tag = data.tag, url = data.url;
                    if (o) {
                        post = o.position().top; posb = post + o.height();
                        if ((post > st && post < sth) || (posb > st && posb < sth)) {
                            if (tag === "img") {
                                o.attr("src", url);
                            } else {
                                o.load(url);
                            }
                            data.obj = null;
                        }
                    }
                });
                return false;
            };

            loading();
            $(window).bind("scroll", loading);
        }
        
        $("img").load(function () {
            //图片默认隐藏  
            $(this).hide();
            //使用fadeIn特效  
            $(this).stop().fadeIn("5000");
        });
        // 异步加载图片，实现逐屏加载图片
        $("img").scrollLoading();
    }());
}
main();