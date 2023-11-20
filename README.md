# chathook

A logical server mod that forwards Minecraft chat, game and command messages to a Discord webhook.

## How to Use

Create a file called `secrets.txt` in the root of your server (in the same folder as the `server.jar`. 

In the file, write the following line:

> `webhook_url=[your webhook url here]`

The webhook URL should be in the form of `https://discord.com/api/webhooks/{some numbers}/{token}`.

Now, just fire up the server and you're all set!


### TODO

1. Commands (/<ch | chathook> [enable | disable | logChat | logGame | logCommands]
2. Live config system (commands change values in HashTable)
3. Rolling config file (Config system saves configs that were modified ingame to a temporary file)
4. Config file parser (move existing secret reading code to seperate package, read configs to HashTable, write modifid values from rolling config file to main config file)
