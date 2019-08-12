const CompressionPlugin = require('compression-webpack-plugin');
const webpack = require('webpack')
module.exports = {
  devServer: {
    host: '127.0.0.1',
    port: 9999,
    proxy: {
      '/zboot': {
        target: 'http://127.0.0.1:8888',  // 请求本地 需要xboot后台项目
        ws: true
      },
      '/foo': {
        target: '<other_url>'
      }
    }
  },
  // 打包时不生成.map文件 避免看到源码
  productionSourceMap: false,
  // 部署优化
  configureWebpack: {
    // 使用CDN
    // externals: {
    //     vue: 'Vue',
    //     axios: 'axios',
    //     'vue-router': 'VueRouter',
    //     vuex: 'Vuex',
    //     iview: 'iview',
    //     echarts: 'echarts',
    //     apexcharts: 'ApexCharts',
    //     'vue-apexcharts': 'VueApexCharts',
    //     xlsx: 'XLSX',
    //     dplayer: 'DPlayer',
    //     gitalk: 'Gitalk'
    // },
    // GZIP压缩
    plugins: [
      new CompressionPlugin({
        test: /\.js$|\.html$|\.css/, // 匹配文件
        threshold: 10240 // 对超过10k文件压缩,
      }),
        new webpack.ProvidePlugin({
            'window.Quill': 'quill/dist/quill.js',
            'Quill': 'quill/dist/quill.js'
        })
    ]
  }
}
