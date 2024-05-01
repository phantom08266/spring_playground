<script setup lang="ts">

import {ref} from "vue";
import axios from "axios";

const title = ref('');
const content = ref('');

const write = function () {
  axios.post("/default-api/api/posts",{
    title: title.value,
    content: content.value
  })
  console.log(title.value, content.value)
  // alert(title + " / " + content)
}
</script>

<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="20"/>
  </div>

  <div>
    <el-button type="primary" @click="write()">글 작성완료</el-button>
  </div>

</template>
<style>
</style>