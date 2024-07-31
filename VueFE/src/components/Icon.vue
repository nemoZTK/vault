<template>
  <div class="icon" @click="navigate">
    <img :src="require(`@/assets/${src}`)" :alt="alt" />
  </div>
</template>

<script>
export default {
  props: {
    src: {
      type: String,
      required: true
    },
    alt: {
      type: String,
      required: true
    },
    link: {
      type: String,
      required: true
    }
  },
  methods: {
    navigate() {
      this.$router.push(this.link);
    }
  }
};
</script>

<style scoped>
.icon {
  width: 300px;
  height: 300px;
  display: inline-block;
  margin: 8.9vw; /* Spazio tra le icone */
  cursor: pointer;
}
.icon img {
  width: 100%;
  height: 100%;
}
</style>
