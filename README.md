4 meter bubble wr
------

# dev

- todo: custom pop particle
- todo: blow sound
- todo: use datagen
- todo: standing on bubbles is still buggy
- bug: reload (unload) desync
- todo: optimize entities
- todo: todos

# ideas

## basic
- [x] hold to blow a bubble, the longer the hold, the bigger the bubble
- [x] bulbble size indicator at the crosshair
- [x] on release it pushes out ~1.5 blocks
- [x] smaller bubbles float upwards
- [x] you can stand on bubbles
- [x] bubbles get destroyed on hit, or after 7-12 seconds
- [x] if the bubble gets interacted with (stood on, entered), it slows its decay by 2x
- [x] bubbles float on water, decay faster when touching a surface
- [x] the reload time is proportional to blowing time

## entering a bubble
- [x] if the bubble can fit an entity, the entity gets inside the bubble
- [x] you can slightly control the horizontal movement of the bubble, but not vertical
- [x] if underwater in a bubble, you can control the vertical movement, and breathe underwater
- [ ] easy break underwater

## enchantments
- [x] if the bubble was blown under an active effect, it gets put on the bubble
- [x] an effect bubble gives the effect indefinetally when inside

- [ ] faster blowing 1-3: bubbles gets bigger faster
- [ ] stronger bubble 1-3: bubbles lasts longer and can withstand more damage
- [ ] single bubble: the bubble is infinite until the player blows another one or unloaded
- [x] bubble barrage: bubbles automatically release when they are small
- [ ] solid bubble: bubbles solidify after a bit

## block
- [ ] bubbles combine into 2x2, 3x3 etc: look at create
- [x] terraria bubble
  
- [ ] bubble machine block

## config
- [ ] model settings