 <template>
  <div style="background-color: white;">
    <div style="padding: 15px;font-size: 20px;">
      数据总览
    </div>

    <div style="margin-top: 20px;">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="total-box">
            <div class="ml-20 text-center total-box-per">
              <div class="total-box-icon fo-w total-box--1">
                <i class="el-icon-user-solid"></i>
              </div>
              <div class="fo-12 fo-9 mt-30">昨日新增</div>
              <div class="fo-12 fo-6">{{ yesterdayUser }}人</div>
            </div>
            <div class="ml-40">
              <div class="fo-14 fo-3">总用户数</div>
              <div class="fo-24 fo-3">{{ allUser }} <span class="fo-12">人</span></div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="total-box">
            <div class="ml-20 text-center total-box-per">
              <div class="total-box-icon fo-w total-box--2">
                <i class="el-icon-s-marketing"></i>
              </div>
              <div class="fo-12 fo-9 mt-30">昨日新增</div>
              <div class="fo-12 fo-6">{{ yesterdayDeal }}元</div>
            </div>
            <div class="ml-40">
              <div class="fo-14 fo-3">总交易额</div>
              <div class="fo-24 fo-3">{{ allDeal }} <span class="fo-12">元</span></div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="total-box">
            <div class="ml-20 text-center total-box-per">
              <div class="total-box-icon fo-w total-box--3">
                <i class="el-icon-s-promotion"></i>
              </div>
              <div class="fo-12 fo-9 mt-30">昨日新增</div>
              <div class="fo-12 fo-6">{{ yesterdayPost }}条</div>
            </div>
            <div class="ml-40">
              <div class="fo-14 fo-3">总发帖量</div>
              <div class="fo-24 fo-3">{{ allPost }} <span class="fo-12">条</span></div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="total-box">
            <div class="ml-20 text-center total-box-per">
              <div class="total-box-icon fo-w total-box--4">
                <i class="el-icon-success"></i>
              </div>
              <div class="fo-12 fo-9 mt-30">昨日新增</div>
              <div class="fo-12 fo-6">{{ yesterdayComplete }}个</div>
            </div>
            <div class="ml-40">
              <div class="fo-14 fo-3">订单完成量</div>
              <div class="fo-24 fo-3">{{ allComplete }} <span class="fo-12">个</span></div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div style="margin-top: 10px;flex: 1">
      <el-row :gutter="10">
        <el-col :span="24">
          <el-card>
            <div style="text-align: center">
              <el-date-picker
                  v-model="date"
                  type="daterange"
                  align="right"
                  unlink-panels
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="yyyy-MM-dd"
                  :default-time="['00:00:00', '23:59:59']"
                  :picker-options="pickerOptions">
              </el-date-picker>
              <el-button type="primary" plain @click="search" style="margin-left: 10px">查  询</el-button>
            </div>
            <div style="width: 100%; height: 350px; margin-top: 10px" id="line"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div style="margin-top: 10px;">
      <el-row :gutter="10">
        <el-col :span="12">
          <el-card>
            <div id="hoop" style="width: 100%; height: 390px"></div>
          </el-card>
        </el-col>

        <el-col :span="12">
          <el-card>
            <div id="pie" style="width: 100%; height: 390px"></div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

const sales = {
  title: {
    text: '销量趋势',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
  },
  legend: {
    center: 'center',
    top: '6%'
  },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      name: '总销售额',
      data: [],
      type: 'line',
      smooth: true,
      lineStyle: {
        normal: {
          color: '#FD9E06'
        }
      },
      itemStyle: {
        normal: {
          color: '#FD9E06'
        }
      },
    },
    {
      name: '商品销售额',
      data: [],
      type: 'line',
      smooth: true,
      lineStyle: {
        normal: {
          color: '#228B22'
        }
      },
      itemStyle: {
        normal: {
          color: '#228B22'
        }
      },
    },
    {
      name: '跑腿销售额',
      data: [],
      type: 'line',
      smooth: true,
    },
    {
      name: '总销量',
      data: [],
      type: 'bar',
      itemStyle: {
        barBorderRadius: [2, 2, 0, 0], //柱体圆角
        color: new echarts.graphic.LinearGradient(
            //前四个参数用于配置渐变色的起止位置，这四个参数依次对应 右下左上 四个方位。也就是从右边开始顺时针方向。
            //通过修改前4个参数，可以实现不同的渐变方向
            /*第五个参数则是一个数组，用于配置颜色的渐变过程。
              每项为一个对象，包含offset和color两个参数
            */
            0, 1, 0, 0, [{//代表渐变色从正上方开始
              offset: 0, //offset范围是0~1，用于表示位置，0是指0%处的颜色
              color: '#409EFF'
            }, //柱图渐变色
              {
                offset: 1, //指100%处的颜色
                color: '#b3d8ff'
              }
            ]
        ),
      }
    },
    {
      name: '商品销量',
      data: [],
      type: 'bar',
    },
    {
      name: '跑腿销量',
      data: [],
      type: 'bar',
    },
  ]
}

const goodsType = {
  title: {
    text: '商品分类销量',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      center: ['50%','55%'],
      radius: ['30%', '50%'],
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + '(' + param.percent +'%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

const errandTypes = {
  title: {
    text: '跑腿分类销量',
    subtext: '比例图',
    left: 'center'
  },
  tooltip: {
    trigger: 'item'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: 'Access From',
      type: 'pie',
      center: ['55%','55%'],
      radius: '50%',
      data: [],
      label: {
        show: true,
        formatter(param) {
          return param.name + '(' + param.percent +'%)';
        }
      },
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
};

export default {
  name: 'Home',
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: [],
      date: [],
      allUser: 0,
      allComplete: 0,
      allDeal: 0,
      allPost: 0,
      yesterdayUser: 0,
      yesterdayComplete: 0,
      yesterdayDeal: 0,
      yesterdayPost: 0,
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            end.setTime(end.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            end.setTime(end.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  mounted() {  //等待页面的元素全部加载完成后再初始化
    this.search()

    this.$request.get("/panel").then(res => {
      if (res.code === "200") {
        this.allUser = res.data.user.allUser
        this.yesterdayUser = res.data.user.yesterdayUser
        this.allDeal = res.data.order.allDeal
        this.yesterdayDeal = res.data.order.yesterdayDeal
        this.allComplete = res.data.complete.allComplete
        this.yesterdayComplete = res.data.complete.yesterdayComplete
        this.allPost = res.data.post.allPost
        this.yesterdayPost = res.data.post.yesterdayPost
      }else {
        this.$message.error(res.msg)  // 弹出错误的信息
      }
    })
  },
  created() {
    this.getDefaultTime()
    this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })
  },
  methods: {
    getDefaultTime() {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      end.setTime(end.getTime() - 3600 * 1000 * 24)
      this.date[0] = `${start.getFullYear()}-${('0' + (start.getMonth() + 1)).slice(-2)}-${('0' + start.getDate()).slice(-2)}`;
      this.date[1] = `${end.getFullYear()}-${('0' + (end.getMonth() + 1)).slice(-2)}-${('0' + end.getDate()).slice(-2)}`;
    },
    search() {
      let lineDom = document.getElementById('line');
      let lineChart = echarts.init(lineDom);

      let hoopDom = document.getElementById('hoop');
      let hoopChart = echarts.init(hoopDom);

      let pieDom = document.getElementById('pie');
      let pieChart = echarts.init(pieDom);

      this.$request.get('/charts', {
        params: {
          startTime: this.date[0],
          endTime: this.date[1]
        }
      }).then(res => {
        if (res.code === '200'){
          // 折线
          sales.xAxis.data = res.data.line?.map(v => v.time) || []
          sales.series[0].data = res.data.line?.map(v => v.allSum) || []
          sales.series[1].data = res.data.line?.map(v => v.goodsSum) || []
          sales.series[2].data = res.data.line?.map(v => v.taskSum) || []
          sales.series[3].data = res.data.line?.map(v => v.allAmount) || []
          sales.series[4].data = res.data.line?.map(v => v.goodsAmount) || []
          sales.series[5].data = res.data.line?.map(v => v.errandAmount) || []
          lineChart.setOption(sales)

          //环图
          goodsType.series[0].data = res.data?.hoop || []
          goodsType.title.subtext = '(' + this.date[0] + '~' +this.date[1] + ')'
          hoopChart.setOption(goodsType)

          // 饼图
          errandTypes.series[0].data = res.data?.pie || []
          errandTypes.title.subtext = '(' + this.date[0] + '~' +this.date[1] + ')'
          pieChart.setOption(errandTypes)
        }
      })
    },
  }
}
</script>
 <style scoped>
 .total-box {
   width: 100%;
   height: 100px;
   box-shadow: 0 0 5px 2px rgba(0, 0, 0, 0.05);
   border-radius: 4px;
   display: flex;
   justify-content: flex-start;
   align-items: center;
   position: relative;
   .total-box-per {
     width: 60px;
   }
   .total-box-icon {
     width: 60px;
     height: 60px;
     border-radius: 4px;
     background: #ffffff;
     line-height: 60px;
     text-align: center;
     position: absolute;
     top: -20px;
     font-size: 30px;
   }
   .total-box--1 {
     background-image: linear-gradient(to bottom right, #f3af19, #ea6439);
   }
   .total-box--2 {
     background-image: linear-gradient(to bottom right, #46aaf4, #385cf4);
   }
   .total-box--3 {
     background-image: linear-gradient(to bottom right, #5ad4e0, #2299d2);
   }
   .total-box--4 {
     background-image: linear-gradient(to bottom right, #4ed855, #3ec296);
   }
 }
 .text-center {
   text-align: center;
 }
 .ml-20 {
   margin-left: 20px;
 }
 .mt-30 {
   margin-top: 30px;
 }
 .ml-40{
   margin-left: 40px;
 }
 .fo-w{
   color: #ffffff;
 }
 .fo-3 {
   color: #333333;
 }
 .fo-6 {
   color: #666666;
 }
 .fo-9 {
   color: #999999;
 }
 .fo-12 {
   font-size: 12px;
 }
 .fo-14 {
   font-size: 14px;
 }
 </style>