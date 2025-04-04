# TODO file

## URGENT

- [x] Make 2 enemy classes
- [x] Make 2 projectile classes
- [x] Implement health and damage taking
- [x] Try to add bloom and add effects
- [x] Fix zoom on camera adn healthbar progress is buggy, FIX TankEnemy shooting dir
- [x] Player controller class
- [x] Make tests for player class
- [x] Comment whole codebase for javadoc
- [] Upload to school github
- [] Write the documentation

### Not urget

- [x] Mouse input
- [x] Polygon drawing
- [x] Check if Vec2 writing is OOP friendly
- [x] Logger
- [x] Rewrite Shape class
- [x] Observer entity creation and deletion (Change update and render to a callback system)
- [x] Create Transform class for child transforms like spaceship shooting point etc
- [x] Projectile working
- [x] Despawning if too far
- [x] Timer class
- [x] ParticleSystem
- [] Collisions
- [] Effect will be a builder class type
- [] Change physics simulation for force based system (include mass)
- [] Color manager (color pallet settings)
- [] Spawner manager
- [] More particle settings
- [] Procedural asteroids
- [] Add exeption handlers (No clue where i could add this)
- [] Add object names? maybe

## Refactoring

Rewrite Shape class using [prototype](https://refactoring.guru/design-patterns/prototype)
Rewrite Asteroid class using [builder](https://refactoring.guru/design-patterns/builder)
Write Particle system using [flyweight](https://refactoring.guru/design-patterns/flyweight)
Refactor input and event handling into a class

## Change the canvas to opengl

! This repo does not work for my notebook so ill ignore it unfortunately
[OpenGlFX](https://github.com/husker-dev/openglfx)

## Collider class design

Collider will have a bounding AABB Box for fast collision checking and then a detailed shape for precise collision.
Detailed collision will be made after <https://github.com/jessevdk/box2d/blob/master/Box2D/Box2D/Collision/b2CollidePolygon.cpp>
