var imgs = ['img/三行情书.jpg', 'img/阅读党史.jpg', 'img/书写经典.jpg', 'img/光盘行动.jpg', 'img/联系我们.jpg'],
	n = imgs.length,
	texts = ['三行情诗&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶',
		'阅读党史&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶',
		'书写经典&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶',
		' 光盘行动&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶',
		'联系我们&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;▶'
	],
	current = n - 1,
	closedWidth = Math.floor(window.innerWidth / 10),
	closedHeight = Math.floor(window.innerHeight / 10)

for (var i = 0; i < n; i++) {
	var bgImg = document.createElement('div')
	bg.appendChild(bgImg)

	gsap.set(bgImg, {
		attr: {
			id: 'bgImg' + i,
			class: 'bgImg'
		},
		width: '100%',
		height: '100%',
		backgroundImage: 'url(' + imgs[i] + ')',
		backgroundSize: 'cover',
		backgroundPosition: 'center',
	})

	var b = document.createElement('div')
	fg.appendChild(b)

	gsap.fromTo(
		b, {
			attr: {
				id: 'b' + i,
				class: 'box'
			},
			innerHTML: '<p><sub>' + (i + 1) + '</sub> ' + texts[i] + '</p>',
			width: '100%',
			height: '100%',
			borderBottom: i > 0 ? 'solid 1px #eee' : '',
			backgroundColor: 'rgba(250,250,250,0)',
			top: i * closedHeight,
			transformOrigin: '100% 100%',
			y: '100%',
		}, {
			duration: i * 0.15,
			y: 0,
			ease: 'expo.inOut',
		}
	)

	b.onmouseenter = b.onclick = (e) => {
		if (Number(e.currentTarget.id.substr(1)) == current) return

		var staggerOrder = !!(current < Number(e.currentTarget.id.substr(1)))
		current = Number(e.currentTarget.id.substr(1))
		gsap.to('.box', {
			duration: 0.5,
			ease: 'elastic.out(0.3)',
			top: (i) => (i <= current ? i * closedHeight : window.innerHeight - (n - i) * closedHeight),
			y: 0,
			stagger: staggerOrder ? 0.05 : -0.05,
		})

		bg.appendChild(document.getElementById('bgImg' + current))
		gsap.fromTo('#bgImg' + current, {
			opacity: 0
		}, {
			opacity: 1,
			duration: 0.3,
			ease: 'power1.inOut'
		})
		gsap.fromTo('#bgImg' + current, {
			scale: 1.05,
			rotation: 0.05
		}, {
			scale: 1,
			rotation: 0,
			duration: 1.5,
			ease: 'sine'
		})
	}
}

window.onresize = (e) => {
	closedHeight = Math.floor(window.innerHeight / 10)
	closedWidth = Math.floor(window.innerWidth / 10)
	gsap.set('.box', {
		y: 0,
		top: (i) => (i <= current ? i * closedHeight : window.innerHeight - (n - i) * closedHeight)
	})
}
