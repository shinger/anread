// src/utils/device.js
/**
 * 判断是否为移动端设备（手机/平板）
 * @returns {Boolean} true=移动端，false=PC端
 */
export const isMobile = () => {
    // 匹配常见的移动端设备UA关键词
    const mobileKeywords = [
        'Android', 'iPhone', 'iPad', 'iPod', 'iOS',
        'BlackBerry', 'Windows Phone', 'Symbian',
        'Mobile', 'Opera Mini', 'UCWEB', 'Fennec',
        'MIUI', 'HarmonyOS' // 鸿蒙系统
    ];
    // 获取UA并转小写，避免大小写问题
    const ua = navigator.userAgent.toLowerCase();
    console.log("ua: ", ua);
    // 匹配关键词，同时排除平板的PC模式（可选）
    return mobileKeywords.some(keyword => ua.includes(keyword.toLowerCase()))
        && !/(Windows|Macintosh|Linux)/.test(ua) // 排除PC端伪装的UA
};

/**
 * 判断是否为PC端
 * @returns {Boolean} true=PC端，false=移动端
 */
export const isPC = () => !isMobile();