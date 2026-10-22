package com.chatwithredis.controller;
import com.chatwithredis.service.PresenceService;
import java.security.Principal;
import java.util.*;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/presence")
public class PresenceController {
 private final PresenceService presence; public PresenceController(PresenceService presence){this.presence=presence;}
 @PostMapping("/heartbeat") public Map<String,String> heartbeat(Principal p){presence.heartbeat(p.getName());return Map.of("status","online");}
 @GetMapping("/online") public List<String> online(){return presence.onlineUsers();}
 @DeleteMapping("/offline") public Map<String,String> offline(Principal p){presence.offline(p.getName());return Map.of("status","offline");}
}