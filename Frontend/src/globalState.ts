import { ref, readonly } from 'vue';

const serverIP = ref('http://192.168.86.40:8080');

export function useGlobalState() {
  return {
    serverIP: readonly(serverIP),
  };
}