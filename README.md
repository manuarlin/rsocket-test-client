### RSocket Test client

To connect client listening on port 7001 to server configured on port 7000 launch:
```shell script
socat -d TCP-LISTEN:7001,fork,reuseaddr TCP:localhost:7000
```

## Resume feature
To test resume feature play with `socat` command given previously to fake connection loss.

