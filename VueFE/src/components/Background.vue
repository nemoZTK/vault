<template>
    <div :style="backgroundStyle"></div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        backgroundUrl: ''
      };
    },
    computed: {
      backgroundStyle() {
        return {
          backgroundImage: `url(${this.backgroundUrl})`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          width: '100%',
          height: '100%',
          position: 'absolute',
          top: 0,
          left: 0,
          zIndex: -1
        };
      }
    },
    created() {
      this.setBackgroundUrl();
    },
    methods: {
      setBackgroundUrl() {
        this.backgroundUrl = 'https://picsum.photos/1200/800?random=' + Math.random();
      }
    }
  };
  </script>
  
  <style scoped>
  div {
    /* Garantisce che il div occupi l'intera viewport e che sia sotto tutti gli elementi*/
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: -1;
  }
  </style>
  