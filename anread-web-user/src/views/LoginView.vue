<template>
  <main>
    <!-- SVG Background -->
    <svg
      class="bg-canvas"
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 1440 900"
      preserveAspectRatio="xMidYMid slice"
    >
      <defs>
        <!-- wood-grain gradient -->
        <linearGradient id="wood" x1="0" y1="0" x2="1" y2="1">
          <stop offset="0%" stop-color="#3d2410" />
          <stop offset="35%" stop-color="#5c3d2e" />
          <stop offset="65%" stop-color="#4a2e1a" />
          <stop offset="100%" stop-color="#2e1a0e" />
        </linearGradient>
        <filter id="woodgrain">
          <feTurbulence
            type="fractalNoise"
            baseFrequency="0.015 0.35"
            numOctaves="5"
            seed="8"
            result="noise"
          />
          <feColorMatrix type="saturate" values="0" in="noise" result="gray" />
          <feBlend
            in="SourceGraphic"
            in2="gray"
            mode="overlay"
            result="blend"
          />
          <feComposite in="blend" in2="SourceGraphic" operator="in" />
        </filter>
        <filter id="softgrain">
          <feTurbulence
            type="fractalNoise"
            baseFrequency="0.65"
            numOctaves="3"
            seed="12"
          />
          <feDisplacementMap
            in="SourceGraphic"
            scale="3"
            xChannelSelector="R"
            yChannelSelector="G"
          />
        </filter>
        <radialGradient id="glow" cx="50%" cy="50%" r="55%">
          <stop offset="0%" stop-color="#7a4f2d" stop-opacity=".5" />
          <stop offset="100%" stop-color="#1a0d05" stop-opacity=".0" />
        </radialGradient>
      </defs>

      <!-- Base wood background -->
      <rect width="1440" height="900" fill="url(#wood)" />
      <rect
        width="1440"
        height="900"
        fill="url(#wood)"
        filter="url(#woodgrain)"
        opacity=".55"
      />
      <rect width="1440" height="900" fill="url(#glow)" />

      <!-- ── Bookshelf – back wall boards ── -->
      <g opacity=".18">
        <rect
          x="0"
          y="0"
          width="1440"
          height="900"
          fill="none"
          stroke="#d4b896"
          stroke-width=".5"
        />
        <!-- horizontal shelf lines -->
        <line
          x1="0"
          y1="210"
          x2="1440"
          y2="210"
          stroke="#c8a87a"
          stroke-width="1.5"
          opacity=".4"
        />
        <line
          x1="0"
          y1="450"
          x2="1440"
          y2="450"
          stroke="#c8a87a"
          stroke-width="1.5"
          opacity=".4"
        />
        <line
          x1="0"
          y1="690"
          x2="1440"
          y2="690"
          stroke="#c8a87a"
          stroke-width="1.5"
          opacity=".4"
        />
        <!-- shelf undersides / thickness -->
        <rect
          x="0"
          y="208"
          width="1440"
          height="10"
          fill="#3a2010"
          opacity=".6"
        />
        <rect
          x="0"
          y="448"
          width="1440"
          height="10"
          fill="#3a2010"
          opacity=".6"
        />
        <rect
          x="0"
          y="688"
          width="1440"
          height="10"
          fill="#3a2010"
          opacity=".6"
        />
      </g>

      <!-- ── Books on shelves ── -->
      <!-- Shelf 1 (y 50–208) -->
      <g opacity=".88" filter="url(#softgrain)">
        <!-- Book 1 -->
        <rect x="28" y="60" width="28" height="148" rx="1" fill="#8b4513" />
        <rect
          x="28"
          y="60"
          width="4"
          height="148"
          fill="#6b3410"
          opacity=".7"
        />
        <line
          x1="42"
          y1="80"
          x2="42"
          y2="190"
          stroke="#f5ead8"
          stroke-width=".6"
          opacity=".3"
        />
        <!-- Book 2 -->
        <rect x="58" y="80" width="22" height="128" rx="1" fill="#b8860b" />
        <rect
          x="58"
          y="80"
          width="3"
          height="128"
          fill="#8b6508"
          opacity=".6"
        />
        <!-- Book 3 – tilted -->
        <g transform="rotate(-4 93 208)">
          <rect x="82" y="78" width="18" height="130" rx="1" fill="#4a7c59" />
          <rect
            x="82"
            y="78"
            width="3"
            height="130"
            fill="#2d5e3d"
            opacity=".6"
          />
        </g>
        <!-- Book 4 -->
        <rect x="102" y="55" width="32" height="153" rx="1" fill="#c0392b" />
        <rect
          x="102"
          y="55"
          width="5"
          height="153"
          fill="#922b21"
          opacity=".7"
        />
        <text
          x="118"
          y="145"
          font-size="8"
          fill="#f5ead8"
          opacity=".45"
          font-family="serif"
          transform="rotate(90 118 145)"
          text-anchor="middle"
        >
          文学集
        </text>
        <!-- Book 5 slim -->
        <rect x="136" y="85" width="14" height="123" rx="1" fill="#2c3e6b" />
        <!-- Book 6 -->
        <rect x="152" y="72" width="26" height="136" rx="1" fill="#d4a574" />
        <rect
          x="152"
          y="72"
          width="4"
          height="136"
          fill="#b8865e"
          opacity=".7"
        />
        <!-- Book 7 -->
        <rect x="180" y="60" width="20" height="148" rx="1" fill="#5d4037" />
        <text
          x="190"
          y="150"
          font-size="7"
          fill="#d4b896"
          opacity=".4"
          font-family="serif"
          transform="rotate(90 190 150)"
          text-anchor="middle"
        >
          历史
        </text>
        <!-- Book 8 slim -->
        <rect x="202" y="90" width="12" height="118" rx="1" fill="#78909c" />
        <!-- Book 9 -->
        <rect x="216" y="58" width="30" height="150" rx="1" fill="#6d4c41" />
        <rect
          x="216"
          y="58"
          width="5"
          height="150"
          fill="#4e342e"
          opacity=".7"
        />
        <!-- Book 10 -->
        <rect x="248" y="75" width="22" height="133" rx="1" fill="#b5651d" />
        <!-- Book 11 leaning -->
        <g transform="rotate(5 280 208)">
          <rect x="272" y="68" width="24" height="140" rx="1" fill="#455a64" />
        </g>
        <!-- Book 12 -->
        <rect x="298" y="62" width="28" height="146" rx="1" fill="#8b0000" />
        <text
          x="312"
          y="148"
          font-size="8"
          fill="#f5ead8"
          opacity=".4"
          font-family="serif"
          transform="rotate(90 312 148)"
          text-anchor="middle"
        >
          诗词
        </text>
        <!-- Book 13 -->
        <rect x="328" y="80" width="18" height="128" rx="1" fill="#7b5e3a" />
        <!-- Book 14 -->
        <rect x="348" y="55" width="36" height="153" rx="1" fill="#3d5a3e" />
        <rect
          x="348"
          y="55"
          width="5"
          height="153"
          fill="#2d432e"
          opacity=".7"
        />
        <!-- ornament / bookend right -->
        <rect
          x="386"
          y="155"
          width="40"
          height="53"
          rx="2"
          fill="#5c3d2e"
          opacity=".7"
        />
        <ellipse
          cx="406"
          cy="145"
          rx="18"
          ry="22"
          fill="#4a2e1a"
          opacity=".7"
        />
        <circle cx="406" cy="145" r="8" fill="#6b4226" opacity=".6" />
        <!-- more books continued -->
        <rect x="432" y="68" width="24" height="140" rx="1" fill="#8b7355" />
        <rect x="458" y="82" width="16" height="126" rx="1" fill="#c17f24" />
        <rect x="476" y="60" width="30" height="148" rx="1" fill="#1a3a5c" />
        <rect x="508" y="78" width="20" height="130" rx="1" fill="#7a3b2e" />
        <rect x="530" y="65" width="28" height="143" rx="1" fill="#4a6741" />
        <rect x="560" y="85" width="14" height="123" rx="1" fill="#9c7b3a" />
        <!-- bookend left-center -->
        <rect
          x="576"
          y="158"
          width="35"
          height="50"
          rx="2"
          fill="#3d2010"
          opacity=".65"
        />
        <!-- gap then more books -->
        <rect x="620" y="70" width="26" height="138" rx="1" fill="#703030" />
        <rect x="648" y="58" width="32" height="150" rx="1" fill="#5a6e3d" />
        <rect x="682" y="80" width="18" height="128" rx="1" fill="#c2956c" />
        <rect x="702" y="65" width="24" height="143" rx="1" fill="#2e4a6e" />
        <rect x="728" y="75" width="20" height="133" rx="1" fill="#8c4a38" />
        <rect x="750" y="62" width="28" height="146" rx="1" fill="#4d7358" />
        <rect
          x="780"
          y="82"
          width="16"
          height="126"
          rx="1"
          fill="#b8860b"
          opacity=".85"
        />
        <rect x="798" y="58" width="34" height="150" rx="1" fill="#6b3a2a" />
        <text
          x="815"
          y="148"
          font-size="8"
          fill="#f5ead8"
          opacity=".35"
          font-family="serif"
          transform="rotate(90 815 148)"
          text-anchor="middle"
        >
          古典文学
        </text>
        <rect x="834" y="72" width="22" height="136" rx="1" fill="#3c5a48" />
        <rect x="858" y="68" width="26" height="140" rx="1" fill="#8b5e3c" />
        <rect x="886" y="80" width="18" height="128" rx="1" fill="#5c2e1a" />
        <rect x="906" y="60" width="30" height="148" rx="1" fill="#7a6030" />
        <rect x="938" y="75" width="20" height="133" rx="1" fill="#2e3a5c" />
        <rect x="960" y="65" width="28" height="143" rx="1" fill="#c06030" />
        <rect x="990" y="78" width="16" height="130" rx="1" fill="#4a5e30" />
        <rect x="1008" y="58" width="32" height="150" rx="1" fill="#7a3a28" />
        <rect x="1042" y="70" width="24" height="138" rx="1" fill="#5c7040" />
        <rect x="1068" y="82" width="18" height="126" rx="1" fill="#b87840" />
        <rect x="1088" y="62" width="26" height="146" rx="1" fill="#2a4860" />
        <rect x="1116" y="72" width="22" height="136" rx="1" fill="#7a4830" />
        <rect x="1140" y="58" width="30" height="150" rx="1" fill="#485a30" />
        <rect x="1172" y="80" width="18" height="128" rx="1" fill="#c08040" />
        <rect x="1192" y="65" width="28" height="143" rx="1" fill="#5c3a60" />
        <rect x="1222" y="75" width="20" height="133" rx="1" fill="#3a5e3a" />
        <rect x="1244" y="60" width="32" height="148" rx="1" fill="#8b4020" />
        <rect x="1278" y="70" width="24" height="138" rx="1" fill="#4060a0" />
        <rect x="1304" y="82" width="16" height="126" rx="1" fill="#7a6840" />
        <rect x="1322" y="62" width="28" height="146" rx="1" fill="#2a5040" />
        <rect x="1352" y="75" width="22" height="133" rx="1" fill="#b05030" />
        <rect x="1376" y="58" width="30" height="150" rx="1" fill="#3a4858" />
        <rect x="1408" y="72" width="32" height="136" rx="1" fill="#6a3820" />
      </g>

      <!-- Shelf 2 (y 460–690) – second row of books -->
      <g opacity=".75" filter="url(#softgrain)">
        <rect
          x="20"
          y="468"
          width="26"
          height="218"
          rx="1"
          fill="#8b4513"
          opacity=".9"
        />
        <rect x="48" y="480" width="20" height="206" rx="1" fill="#6d9e5a" />
        <rect x="70" y="465" width="30" height="221" rx="1" fill="#b05a2a" />
        <rect x="102" y="478" width="18" height="208" rx="1" fill="#3a5880" />
        <rect x="122" y="460" width="34" height="226" rx="1" fill="#7a5030" />
        <text
          x="139"
          y="590"
          font-size="9"
          fill="#f5ead8"
          opacity=".35"
          font-family="serif"
          transform="rotate(90 139 590)"
          text-anchor="middle"
        >
          山海经
        </text>
        <rect x="158" y="472" width="22" height="214" rx="1" fill="#5a7a30" />
        <rect x="182" y="468" width="26" height="218" rx="1" fill="#8a4040" />
        <rect x="210" y="482" width="16" height="204" rx="1" fill="#c0a060" />
        <rect x="228" y="462" width="32" height="224" rx="1" fill="#3a4a6a" />
        <rect x="262" y="475" width="20" height="211" rx="1" fill="#7a3820" />
        <!-- bookend -->
        <rect
          x="284"
          y="628"
          width="38"
          height="60"
          rx="2"
          fill="#2e1a0e"
          opacity=".7"
        />
        <ellipse
          cx="303"
          cy="612"
          rx="20"
          ry="24"
          fill="#3d2010"
          opacity=".65"
        />
        <rect x="330" y="470" width="28" height="216" rx="1" fill="#4a6040" />
        <rect x="360" y="480" width="18" height="206" rx="1" fill="#c87820" />
        <rect x="380" y="462" width="30" height="224" rx="1" fill="#3a5848" />
        <rect x="412" y="475" width="22" height="211" rx="1" fill="#8b3a2e" />
        <rect x="436" y="465" width="26" height="221" rx="1" fill="#506888" />
        <rect x="464" y="478" width="20" height="208" rx="1" fill="#6a8040" />
        <rect x="486" y="460" width="34" height="226" rx="1" fill="#7a4a28" />
        <rect x="522" y="472" width="18" height="214" rx="1" fill="#3c5c3c" />
        <rect x="542" y="468" width="28" height="218" rx="1" fill="#a06030" />
        <rect x="572" y="480" width="16" height="206" rx="1" fill="#2a3a60" />
        <rect x="590" y="462" width="32" height="224" rx="1" fill="#6a4030" />
        <!-- continued right side -->
        <rect x="680" y="465" width="28" height="221" rx="1" fill="#5a7040" />
        <rect x="710" y="478" width="20" height="208" rx="1" fill="#a84828" />
        <rect x="732" y="462" width="32" height="224" rx="1" fill="#385868" />
        <rect x="766" y="474" width="22" height="212" rx="1" fill="#7a5828" />
        <rect x="790" y="468" width="26" height="218" rx="1" fill="#4a3a70" />
        <rect x="818" y="480" width="18" height="206" rx="1" fill="#6e8a30" />
        <rect x="838" y="462" width="30" height="224" rx="1" fill="#9c4030" />
        <rect x="870" y="474" width="20" height="212" rx="1" fill="#305868" />
        <rect x="892" y="465" width="28" height="221" rx="1" fill="#8a6828" />
        <rect x="922" y="478" width="16" height="208" rx="1" fill="#3a5a3a" />
        <rect x="940" y="460" width="34" height="226" rx="1" fill="#6a3820" />
        <text
          x="957"
          y="598"
          font-size="9"
          fill="#f5ead8"
          opacity=".32"
          font-family="serif"
          transform="rotate(90 957 598)"
          text-anchor="middle"
        >
          唐诗三百首
        </text>
        <rect x="976" y="472" width="22" height="214" rx="1" fill="#4a6848" />
        <rect x="1000" y="468" width="26" height="218" rx="1" fill="#b04828" />
        <rect x="1060" y="465" width="28" height="221" rx="1" fill="#3a5060" />
        <rect x="1090" y="478" width="20" height="208" rx="1" fill="#7a4828" />
        <rect x="1112" y="462" width="30" height="224" rx="1" fill="#506040" />
        <rect x="1144" y="474" width="22" height="212" rx="1" fill="#c06838" />
        <rect x="1168" y="468" width="26" height="218" rx="1" fill="#2a4858" />
        <rect x="1196" y="480" width="18" height="206" rx="1" fill="#6a5028" />
        <rect x="1216" y="462" width="30" height="224" rx="1" fill="#3a5a3e" />
        <rect x="1248" y="474" width="22" height="212" rx="1" fill="#8c3828" />
        <rect x="1272" y="468" width="26" height="218" rx="1" fill="#48607a" />
        <rect x="1300" y="480" width="18" height="206" rx="1" fill="#7a6038" />
        <rect x="1320" y="462" width="32" height="224" rx="1" fill="#3a4830" />
        <rect x="1354" y="474" width="20" height="212" rx="1" fill="#b05838" />
        <rect x="1376" y="465" width="28" height="221" rx="1" fill="#2a3850" />
        <rect x="1406" y="478" width="34" height="208" rx="1" fill="#6a4028" />
      </g>

      <!-- ── Open book center-bottom decoration ── -->
      <g transform="translate(660 810)" opacity=".22">
        <!-- left page -->
        <path
          d="M0 0 Q-90 -8 -140 -60 L-140 60 Q-90 48 0 55 Z"
          fill="#f5ead8"
          stroke="#c8a87a"
          stroke-width="1"
        />
        <!-- right page -->
        <path
          d="M0 0 Q90 -8 140 -60 L140 60 Q90 48 0 55 Z"
          fill="#ede3cc"
          stroke="#c8a87a"
          stroke-width="1"
        />
        <!-- lines on pages -->
        <g stroke="#b8956a" stroke-width=".6" opacity=".5">
          <line x1="-120" y1="-35" x2="-15" y2="-32" />
          <line x1="-120" y1="-20" x2="-15" y2="-18" />
          <line x1="-120" y1="-5" x2="-15" y2="-4" />
          <line x1="-120" y1="10" x2="-15" y2="10" />
          <line x1="-120" y1="25" x2="-15" y2="24" />
          <line x1="15" y1="-35" x2="120" y2="-32" />
          <line x1="15" y1="-20" x2="120" y2="-18" />
          <line x1="15" y1="-5" x2="120" y2="-4" />
          <line x1="15" y1="10" x2="120" y2="10" />
          <line x1="15" y1="25" x2="120" y2="24" />
        </g>
        <!-- spine -->
        <line
          x1="0"
          y1="-60"
          x2="0"
          y2="60"
          stroke="#c8a87a"
          stroke-width="1.5"
        />
      </g>

      <!-- ── Quill pen ── -->
      <g transform="translate(1280 760) rotate(-35)" opacity=".2">
        <path
          d="M0 0 C20 -60 60 -90 90 -80 C70 -50 50 -20 40 0 Z"
          fill="#f5ead8"
        />
        <path
          d="M40 0 L38 30"
          stroke="#c8a87a"
          stroke-width="1.2"
          fill="none"
        />
        <path
          d="M40 0 C42 -20 55 -30 65 -25"
          stroke="#d4b896"
          stroke-width=".8"
          fill="none"
        />
      </g>

      <!-- ── Reading glasses ── -->
      <g
        transform="translate(180 820)"
        opacity=".18"
        fill="none"
        stroke="#d4b896"
        stroke-width="1.5"
      >
        <circle cx="-30" cy="0" r="22" />
        <circle cx="30" cy="0" r="22" />
        <line x1="-8" y1="0" x2="8" y2="0" />
        <line x1="-52" y1="-8" x2="-70" y2="-18" />
        <line x1="52" y1="-8" x2="70" y2="-18" />
      </g>

      <!-- ── Tea cup ── -->
      <g transform="translate(1350 840)" opacity=".17">
        <path
          d="M-28 -12 Q-30 18 -20 22 L20 22 Q30 18 28 -12 Z"
          fill="#8b5e3c"
          stroke="#c8a87a"
          stroke-width="1"
        />
        <path
          d="M28 -5 Q44 -2 44 8 Q44 18 28 14"
          fill="none"
          stroke="#c8a87a"
          stroke-width="1.2"
        />
        <ellipse
          cx="0"
          cy="-12"
          rx="28"
          ry="7"
          fill="#9b6e4c"
          stroke="#c8a87a"
          stroke-width=".8"
        />
        <!-- steam -->
        <path
          d="M-10 -22 Q-8 -34 -10 -44"
          stroke="#e8d9c0"
          stroke-width="1"
          fill="none"
          opacity=".5"
          stroke-linecap="round"
        />
        <path
          d="M0 -24 Q2 -38 0 -50"
          stroke="#e8d9c0"
          stroke-width="1"
          fill="none"
          opacity=".5"
          stroke-linecap="round"
        />
        <path
          d="M10 -22 Q12 -34 10 -44"
          stroke="#e8d9c0"
          stroke-width="1"
          fill="none"
          opacity=".5"
          stroke-linecap="round"
        />
      </g>

      <!-- Ambient vignette -->
      <radialGradient id="vignette" cx="50%" cy="50%" r="70%">
        <stop offset="50%" stop-color="transparent" />
        <stop offset="100%" stop-color="#0e0704" stop-opacity=".75" />
      </radialGradient>
      <rect width="1440" height="900" fill="url(#vignette)" />

      <!-- warm center light -->
      <radialGradient id="centerlight" cx="50%" cy="50%" r="40%">
        <stop offset="0%" stop-color="#c8864a" stop-opacity=".12" />
        <stop offset="100%" stop-color="transparent" />
      </radialGradient>
      <rect width="1440" height="900" fill="url(#centerlight)" />
    </svg>

    <!-- Grain -->
    <div class="grain"></div>

    <!-- Floating dust -->
    <div class="dust" ref="dustContainer"></div>

    <!-- Main page -->
    <div class="page">
      <!-- Left decorative panel -->
      <div class="decor">
        <svg
          class="decor-svg"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 380 540"
          preserveAspectRatio="xMidYMid slice"
        >
          <defs>
            <linearGradient id="dwood" x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stop-color="#3a2010" />
              <stop offset="50%" stop-color="#5c3d2e" />
              <stop offset="100%" stop-color="#2e1a0e" />
            </linearGradient>
            <filter id="dg">
              <feTurbulence
                type="fractalNoise"
                baseFrequency="0.02 0.4"
                numOctaves="4"
                seed="5"
              />
              <feColorMatrix type="saturate" values="0" />
              <feBlend in="SourceGraphic" mode="overlay" />
              <feComposite in2="SourceGraphic" operator="in" />
            </filter>
          </defs>
          <rect width="380" height="540" fill="url(#dwood)" />
          <rect
            width="380"
            height="540"
            fill="url(#dwood)"
            filter="url(#dg)"
            opacity=".6"
          />

          <!-- stacked books illustration -->
          <!-- stack on floor -->
          <g transform="translate(60 420)">
            <rect
              x="0"
              y="0"
              width="110"
              height="20"
              rx="2"
              fill="#8b4513"
              opacity=".9"
            />
            <rect
              x="5"
              y="-18"
              width="100"
              height="20"
              rx="2"
              fill="#c0392b"
              opacity=".9"
            />
            <rect
              x="10"
              y="-36"
              width="92"
              height="20"
              rx="2"
              fill="#2c6e4a"
              opacity=".9"
            />
            <rect
              x="8"
              y="-54"
              width="96"
              height="20"
              rx="2"
              fill="#8b7355"
              opacity=".9"
            />
            <!-- pages edge -->
            <rect
              x="112"
              y="0"
              width="6"
              height="20"
              rx="1"
              fill="#f5ead8"
              opacity=".7"
            />
            <rect
              x="107"
              y="-18"
              width="6"
              height="20"
              rx="1"
              fill="#f5ead8"
              opacity=".7"
            />
            <rect
              x="104"
              y="-36"
              width="6"
              height="20"
              rx="1"
              fill="#f5ead8"
              opacity=".7"
            />
            <rect
              x="106"
              y="-54"
              width="6"
              height="20"
              rx="1"
              fill="#f5ead8"
              opacity=".7"
            />
          </g>

          <!-- open book on top -->
          <g transform="translate(60 364) rotate(-5)">
            <path
              d="M0 0 Q-40 -4 -60 -28 L-60 28 Q-40 22 0 25 Z"
              fill="#f5ead8"
              opacity=".85"
            />
            <path
              d="M0 0 Q40 -4 60 -28 L60 28 Q40 22 0 25 Z"
              fill="#ede3cc"
              opacity=".85"
            />
            <g stroke="#b8956a" stroke-width=".5" opacity=".6">
              <line x1="-52" y1="-14" x2="-6" y2="-12" />
              <line x1="-52" y1="-4" x2="-6" y2="-3" />
              <line x1="-52" y1="6" x2="-6" y2="6" />
              <line x1="-52" y1="16" x2="-6" y2="15" />
              <line x1="6" y1="-14" x2="52" y2="-12" />
              <line x1="6" y1="-4" x2="52" y2="-3" />
              <line x1="6" y1="6" x2="52" y2="6" />
              <line x1="6" y1="16" x2="52" y2="15" />
            </g>
            <line
              x1="0"
              y1="-28"
              x2="0"
              y2="28"
              stroke="#c8a87a"
              stroke-width="1"
            />
          </g>

          <!-- bookmark ribbon -->
          <g transform="translate(220 120)" opacity=".7">
            <rect x="-5" y="0" width="10" height="80" fill="#c0392b" />
            <path d="M-5 80 L0 68 L5 80 Z" fill="#c0392b" />
          </g>

          <!-- candle -->
          <g transform="translate(290 310)" opacity=".8">
            <rect
              x="-8"
              y="0"
              width="16"
              height="60"
              rx="2"
              fill="#f5ead8"
              opacity=".7"
            />
            <rect
              x="-8"
              y="0"
              width="16"
              height="8"
              rx="1"
              fill="#e8d9c0"
              opacity=".5"
            />
            <!-- flame -->
            <ellipse
              cx="0"
              cy="-12"
              rx="5"
              ry="9"
              fill="#f5c842"
              opacity=".85"
            />
            <ellipse
              cx="0"
              cy="-14"
              rx="3"
              ry="6"
              fill="#fff9e6"
              opacity=".9"
            />
            <line
              x1="0"
              y1="0"
              x2="0"
              y2="-4"
              stroke="#8b6508"
              stroke-width="1.5"
            />
            <!-- glow -->
            <radialGradient id="flame" cx="50%" cy="40%" r="60%">
              <stop offset="0%" stop-color="#f5c842" stop-opacity=".4" />
              <stop offset="100%" stop-color="transparent" />
            </radialGradient>
            <ellipse cx="0" cy="-10" rx="30" ry="40" fill="url(#flame)" />
          </g>

          <!-- magnifier -->
          <g
            transform="translate(120 230) rotate(20)"
            opacity=".5"
            fill="none"
            stroke="#d4b896"
            stroke-width="2"
          >
            <circle cx="0" cy="0" r="28" />
            <line x1="20" y1="20" x2="44" y2="44" />
          </g>

          <!-- quill at top -->
          <g transform="translate(200 80) rotate(-40)" opacity=".55">
            <path
              d="M0 0 C15 -45 45 -70 70 -62 C52 -38 36 -14 28 0 Z"
              fill="#f5ead8"
            />
            <path
              d="M28 0 L26 22"
              stroke="#c8a87a"
              stroke-width="1"
              fill="none"
            />
          </g>

          <!-- decorative corner ornaments -->
          <g stroke="#c8a87a" stroke-width="1" fill="none" opacity=".4">
            <path d="M20 20 L50 20 M20 20 L20 50" />
            <path d="M360 20 L330 20 M360 20 L360 50" />
            <path d="M20 520 L50 520 M20 520 L20 490" />
            <path d="M360 520 L330 520 M360 520 L360 490" />
          </g>

          <!-- top quote -->
          <text
            x="190"
            y="48"
            font-size="11"
            fill="#d4b896"
            opacity=".55"
            font-family="'Noto Serif SC', serif"
            text-anchor="middle"
            letter-spacing="3"
          >
            書中自有黃金屋
          </text>

          <!-- Vignette overlay -->
          <radialGradient id="dv" cx="50%" cy="50%" r="70%">
            <stop offset="50%" stop-color="transparent" />
            <stop offset="100%" stop-color="#0e0704" stop-opacity=".5" />
          </radialGradient>
          <rect width="380" height="540" fill="url(#dv)" />
        </svg>

        <div class="decor-footer">
          <div class="brand">安阅书屋</div>
          <div class="tagline">好書如良友 · 伴你左右</div>
        </div>
        <div class="spine"></div>
      </div>

      <!-- Right form panel -->
      <div class="panel">
        <!-- Tabs -->
        <div class="tabs">
          <button
            class="tab-btn"
            :class="{ active: activeTab === 'login' }"
            @click="switchTab('login')"
          >
            登 录
          </button>
          <button
            class="tab-btn"
            :class="{ active: activeTab === 'register' }"
            @click="switchTab('register')"
          >
            注 册
          </button>
        </div>

        <!-- Login form -->
        <div
          class="tab-panel"
          :class="{ active: activeTab === 'login' }"
          id="tab-login"
        >
          <div class="form">
            <div class="form-group">
              <label>邮箱 / 用户名</label>
              <input
                type="text"
                v-model="loginForm.email"
                placeholder="请输入您的账号"
              />
            </div>
            <div class="form-group">
              <label>密 码</label>
              <input
                type="password"
                v-model="loginForm.password"
                placeholder="请输入密码"
              />
            </div>
            <div
              style="display: flex; justify-content: flex-end; margin-top: -6px"
            >
              <a
                style="
                  font-size: 0.7rem;
                  color: var(--cedar);
                  opacity: 0.6;
                  text-decoration: none;
                  letter-spacing: 0.08em;
                  cursor: pointer;
                  border-bottom: 1px dashed var(--cedar);
                "
                >忘记密码？</a
              >
            </div>
            <button class="btn-submit" @click="handleLogin">
              <span>进 入 书 房</span>
            </button>
            <div class="or-line">或</div>
            <div class="form-footer">
              还没有账号？<a @click="switchTab('register')">立即注册</a
              >，开启阅读之旅
            </div>
          </div>
        </div>

        <!-- Register form -->
        <div
          class="tab-panel"
          :class="{ active: activeTab === 'register' }"
          id="tab-register"
        >
          <div class="form">
            <div class="form-group">
              <label>用 户 名</label>
              <input
                type="text"
                v-model="registerForm.username"
                placeholder="取一个好听的名字"
              />
            </div>
            <div class="form-group">
              <label>邮 箱</label>
              <input
                type="email"
                v-model="registerForm.email"
                placeholder="用于找回密码"
              />
            </div>
            <div class="form-group">
              <label>密 码</label>
              <input
                type="password"
                v-model="registerForm.password"
                placeholder="至少 8 位字符"
              />
            </div>
            <div class="form-group">
              <label>确认密码</label>
              <input
                type="password"
                v-model="registerForm.confirmPassword"
                placeholder="再次输入密码"
              />
            </div>
            <button
              class="btn-submit"
              style="margin-top: 4px"
              @click="handleRegister"
            >
              <span>加 入 书 卷</span>
            </button>
            <div class="form-footer">
              已有账号？<a @click="switchTab('login')">直接登录</a>
            </div>
          </div>
        </div>

        <div class="page-num">p. 001</div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, reactive, onMounted, onBeforeMount } from "vue";
import { useRouter } from "vue-router";
import { userLogin, userRegister } from "@/api/user-api.js";
import { useToast } from "vue-toast-notification";
import { useLoginStore } from "@/stores/login";

// 激活的标签页
const activeTab = ref("login");
const loginStore = useLoginStore();
const toast = useToast();
const router = useRouter();

// 登录表单数据
const loginForm = reactive({
  email: "",
  password: "",
});

// 注册表单数据
const registerForm = reactive({
  username: "",
  email: "",
  password: "",
  confirmPassword: "",
});

onBeforeMount(() => {
  loginStore.updateLogin();
  if (loginStore.isLogin.value) {
    router.push("/");
  }
});

const registerFormValid = () => {
  if (!registerForm.username.match("^[\u4e00-\u9fa5A-Za-z0-9]{2,15}$")) {
    toast.error("用户名为2-15个字符");
    return false;
  }
  if (
    !registerForm.email.match(
      "^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(.[a-zA-Z0-9_-]+)+$"
    )
  ) {
    toast.error("邮箱格式错误");
    return false;
  }
  if (!registerForm.password.match("^[A-Za-z0-9]{6,15}$")) {
    toast.error("密码应为6-15个英文或数字");
    return false;
  }
  if (registerForm.password !== registerForm.confirmPassword) {
    toast.error("两次输入的密码不一致");
    return false;
  }
  return true;
};

const handleRegister = () => {
  if (!registerFormValid()) {
    return;
  }
  userRegister(registerForm).then((result) => {
    localStorage.setItem("token", result);
    loginStore.updateLogin();
    toast.success("注册成功");
    router.push("/");
  });
};

const handleLogin = () => {
  if (loginForm.email == "" || loginForm.password == "") {
    toast.error("邮箱和密码不能为空");
    return;
  }
  userLogin(loginForm).then((result) => {
    console.log(result);
    localStorage.setItem("token", result);
    loginStore.updateLogin();
    toast.success("登录成功");
    router.push("/");
    // location.reload();
  });
};

// 粉尘容器引用
const dustContainer = ref(null);

// 切换标签页
const switchTab = (tabId) => {
  activeTab.value = tabId;
};

// 创建粉尘粒子效果
const createDustParticles = () => {
  if (!dustContainer.value) return;

  // 清空现有粒子
  dustContainer.value.innerHTML = "";

  // 创建22个粉尘粒子
  for (let i = 0; i < 22; i++) {
    const mote = document.createElement("div");
    mote.className = "mote";
    const size = Math.random() * 3 + 1;
    mote.style.cssText = `
      width:${size}px; height:${size}px;
      left:${Math.random() * 100}%;
      animation-duration:${Math.random() * 18 + 12}s;
      animation-delay:${Math.random() * 15}s;
    `;
    dustContainer.value.appendChild(mote);
  }
};

// 组件挂载时创建粉尘粒子
onMounted(() => {
  createDustParticles();
});
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

main {
  min-height: 100vh;
  font-family: "Noto Serif SC", serif;
  background-color: var(--bark);
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 100;
}

/* ─── SVG Background ─── */
.bg-canvas {
  position: fixed;
  inset: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

/* ─── Grain overlay ─── */
.grain {
  position: fixed;
  inset: 0;
  z-index: 1;
  pointer-events: none;
  opacity: 0.45;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 256 256' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='1'/%3E%3C/svg%3E");
  background-size: 180px;
}

/* ─── Floating dust particles ─── */
::v-deep .dust {
  position: fixed;
  inset: 0;
  z-index: 2;
  pointer-events: none;
}

::v-deep .mote {
  position: absolute;
  border-radius: 50%;
  background: var(--parchment);
  opacity: 0;
  animation: drift linear infinite;
}

@keyframes drift {
  0% {
    transform: translateY(100vh) translateX(0) scale(1);
    opacity: 0;
  }

  10% {
    opacity: 0.6;
  }

  90% {
    opacity: 0.3;
  }

  100% {
    transform: translateY(-10vh) translateX(30px) scale(0.5);
    opacity: 0;
  }
}

/* ─── Page wrapper ─── */
.page {
  position: relative;
  z-index: 10;
  display: flex;
  gap: 0;
  width: min(900px, 95vw);
  min-height: 540px;
  border-radius: 4px;
  overflow: hidden;
  box-shadow: 0 30px 90px rgba(30, 15, 5, 0.55), 0 2px 6px rgba(30, 15, 5, 0.3);
  animation: pageIn 1.1s cubic-bezier(0.22, 1, 0.36, 1) both;
}

@keyframes pageIn {
  from {
    opacity: 0;
    transform: translateY(32px) scale(0.97);
  }

  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* ─── Left decorative panel ─── */
.decor {
  width: 42%;
  background: var(--bark);
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  padding: 40px 32px;
  overflow: hidden;
}

.decor-svg {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
}

.decor-footer {
  position: relative;
  z-index: 2;
  text-align: center;
  color: var(--parchment);
}

.decor-footer .brand {
  font-family: "Ma Shan Zheng", cursive;
  font-size: 2.2rem;
  letter-spacing: 0.15em;
  text-shadow: 0 2px 12px rgba(0, 0, 0, 0.4);
  line-height: 1;
}

.decor-footer .tagline {
  margin-top: 10px;
  font-size: 0.72rem;
  letter-spacing: 0.28em;
  color: var(--birch);
  opacity: 0.85;
}

/* spine line */
.spine {
  position: absolute;
  top: 0;
  right: 0;
  width: 3px;
  height: 100%;
  background: linear-gradient(
    to bottom,
    transparent 0%,
    var(--cedar) 20%,
    var(--cedar) 80%,
    transparent 100%
  );
  opacity: 0.5;
}

/* ─── Right form panel ─── */
.panel {
  flex: 1;
  background: var(--cream);
  padding: 48px 44px 44px;
  display: flex;
  flex-direction: column;
  position: relative;
}

/* subtle inner texture */
.panel::before {
  content: "";
  position: absolute;
  inset: 0;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='6' height='6'%3E%3Crect width='6' height='6' fill='%23faf3e8'/%3E%3Ccircle cx='3' cy='3' r='.45' fill='%23d4b896' opacity='.35'/%3E%3C/svg%3E");
  pointer-events: none;
  z-index: 0;
}

.panel > * {
  position: relative;
  z-index: 1;
}

/* ─── Tab switch ─── */
.tabs {
  display: flex;
  gap: 0;
  border-bottom: 1.5px solid var(--aged);
  margin-bottom: 36px;
}

.tab-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-family: "Noto Serif SC", serif;
  font-size: 0.82rem;
  letter-spacing: 0.18em;
  color: var(--cedar);
  padding: 8px 20px 12px;
  position: relative;
  transition: color 0.3s;
  opacity: 0.55;
}

.tab-btn.active {
  opacity: 1;
  color: var(--bark);
}

.tab-btn.active::after {
  content: "";
  position: absolute;
  bottom: -1.5px;
  left: 0;
  right: 0;
  height: 2px;
  background: var(--mahogany);
  border-radius: 2px 2px 0 0;
}

/* ─── Form ─── */
.form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label {
  font-size: 0.72rem;
  letter-spacing: 0.22em;
  color: var(--cedar);
  font-weight: 500;
}

input {
  background: transparent;
  border: none;
  border-bottom: 1.5px solid var(--aged);
  padding: 10px 2px;
  font-family: "Noto Serif SC", serif;
  font-size: 0.92rem;
  color: var(--ink);
  outline: none;
  transition: border-color 0.3s, padding-left 0.2s;
  caret-color: var(--mahogany);
}

input::placeholder {
  color: var(--birch);
  opacity: 0.7;
  font-size: 0.85rem;
}

input:focus {
  border-bottom-color: var(--mahogany);
  padding-left: 4px;
}

/* ─── Submit button ─── */
.btn-submit {
  margin-top: 8px;
  border: 1.5px solid var(--mahogany);
  background: transparent;
  color: var(--mahogany);
  font-family: "Noto Serif SC", serif;
  font-size: 0.8rem;
  letter-spacing: 0.3em;
  padding: 13px 0;
  cursor: pointer;
  transition: background 0.3s, color 0.3s, transform 0.15s;
  border-radius: 1px;
  position: relative;
  overflow: hidden;
}

.btn-submit::before {
  content: "";
  position: absolute;
  inset: 0;
  background: var(--mahogany);
  transform: translateX(-101%);
  transition: transform 0.35s cubic-bezier(0.22, 1, 0.36, 1);
  z-index: 0;
}

.btn-submit span {
  position: relative;
  z-index: 1;
}

.btn-submit:hover::before {
  transform: translateX(0);
}

.btn-submit:hover {
  color: var(--cream);
}

.btn-submit:active {
  transform: scale(0.98);
}

/* ─── Footer link ─── */
.form-footer {
  margin-top: 22px;
  font-size: 0.72rem;
  color: var(--cedar);
  opacity: 0.7;
  letter-spacing: 0.08em;
  display: flex;
  gap: 4px;
  align-items: center;
}

.form-footer a {
  color: var(--mahogany);
  text-decoration: none;
  cursor: pointer;
  border-bottom: 1px dashed var(--cedar);
  padding-bottom: 1px;
  transition: opacity 0.2s;
}

.form-footer a:hover {
  opacity: 0.7;
}

/* ─── Tab panels ─── */
.tab-panel {
  display: none;
}

.tab-panel.active {
  display: block;
}

/* ─── Divider ─── */
.or-line {
  display: flex;
  align-items: center;
  gap: 12px;
  color: var(--cedar);
  font-size: 0.68rem;
  letter-spacing: 0.2em;
  opacity: 0.5;
  margin: 4px 0;
}

.or-line::before,
.or-line::after {
  content: "";
  flex: 1;
  height: 1px;
  background: var(--aged);
}

/* ─── Page number corner ─── */
.page-num {
  position: absolute;
  bottom: 16px;
  right: 20px;
  font-size: 0.62rem;
  color: var(--cedar);
  opacity: 0.35;
  letter-spacing: 0.15em;
  font-style: italic;
}

/* ─── Responsive ─── */
@media (max-width: 620px) {
  .decor {
    display: none;
  }

  .panel {
    padding: 40px 28px 36px;
  }
}

/* 引入外部字体（如果需要） */
@font-face {
  font-family: "Noto Serif SC";
  src: url("https://fonts.googleapis.com/css2?family=Noto+Serif+SC:wght@300;400;500;600&display=swap");
}

@font-face {
  font-family: "ZCOOL XiaoWei";
  src: url("https://fonts.googleapis.com/css2?family=ZCOOL+XiaoWei&display=swap");
}

@font-face {
  font-family: "Ma Shan Zheng";
  src: url("https://fonts.googleapis.com/css2?family=Ma+Shan+Zheng&display=swap");
}
</style>