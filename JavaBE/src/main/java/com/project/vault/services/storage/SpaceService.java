package com.project.vault.services.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.vault.entities.VaultSpace;
import com.project.vault.entities.VaultUser;
import com.project.vault.repos.SpaceRepository;
import com.project.vault.services.VaultUserAuthenticationService;

@Service
public class SpaceService implements SpaceServiceInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	SpaceRepository spaceRepo;
	@Autowired
	VaultUserAuthenticationService userAuthServ;
	@Autowired
	FileManagerService fileManagerServ;

	public JSONObject holdNewSpaceRequest(String name, Long userId) {
		JSONObject response = new JSONObject();
		if (!userAuthServ.existById(userId)) {
			return response.put("result", "user not found " + userId);
		}
		VaultSpace space = saveNewSpace(name, userId);
		if (space == null) {
			return response.put("result", "local folder creation failed for space request " + name);
		} else {
			return response.put("result", "done").put("id", space.getId()).put("name", name);
		}
	}

	@Override
	public Boolean isHimTheSpaceOwner(Long spaceId, Long userId) {
		return spaceRepo.existsByIdAndUserId(spaceId, userId);
	}

	public VaultSpace getSpaceById(Long id) {
		if (id != null && spaceRepo.existsById(id)) {
			return spaceRepo.findById(id).get();

		}
		logger.error("[" + id + "] not found you've passed a invalid space id.");
		return null;
	}

	public Boolean existsById(Long id) {
		return spaceRepo.existsById(id);
	}

	public String findNameById(Long id) {
		return spaceRepo.findNameById(id);
	}

	public VaultSpace cleanVaultSpace(VaultSpace space) {
		space.setVaultUser(null);
		space.setVaultUser(null);
		return space;
	}

	public List<VaultSpace> getSpaceListByUserId(Long userId) {
		List<VaultSpace> spaces = spaceRepo.findByVaultUserId(userId);
		for (VaultSpace space : spaces) {
			cleanVaultSpace(space);
		}
		return spaces;

	}

	// ------------------------------------SPACE--------------------------------------------------------------------------------
	public VaultSpace saveNewSpace(String name, Long userId) {
		JSONObject response = new JSONObject();
		Boolean isDone = false;
		VaultSpace space = new VaultSpace();
		VaultUser user = null;
		space.setCreatedAt(LocalDateTime.now());
		space.setName(name);

		user = userAuthServ.getUserById(userId);
		if (user == null) {
			// no user
			return null;
		}
		String knownPath = "/" + user.getUsername() + "/" + space.getName();
		isDone = fileManagerServ.createFolder(knownPath);
		if (isDone) {
			try {
				space.setVaultUser(user);
				spaceRepo.save(space);
				return space;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
