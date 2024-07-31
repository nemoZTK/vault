const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = {
  devServer: {
    host: '0.0.0.0', // Ascolta su tutte le interfacce
    port: 8686,      // Puoi cambiare la porta se necessario
  },
};