Open the loot_tables folder in the plugin directory.  Make a new file called `somename.yml`.

Edit the file with your favorite text editor (preferably not notepad).

Here are some samples of what you could put in the file:

```
apples:
  id: APPLE
  chance: .5
  rolls: 4
  amount: 3
a_sword:
  id: IRON_SWORD
  chance: .3
  rolls: 1
```

In game, target a chest and type `/cr set loot_table somename.yml`.

This will tell the chest to restock with:
1-4 apples
1 iron sword
