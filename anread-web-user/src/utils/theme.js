// utils/theme.js
export function watchSystemTheme(callback) {
  const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
  
  const handleChange = (e) => {
    const isDarkMode = e.matches;
    callback(isDarkMode ? 'dark' : 'light');
  };

  // 初始调用
  handleChange(mediaQuery);

  // 监听变化
  mediaQuery.addEventListener('change', handleChange);

  // 返回取消监听函数
  return () => {
    mediaQuery.removeEventListener('change', handleChange);
  };
}