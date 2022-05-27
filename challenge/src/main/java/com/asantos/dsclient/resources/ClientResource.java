package com.asantos.dsclient.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.asantos.dsclient.dto.ClientDTO;
import com.asantos.dsclient.services.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientResource {

	@Autowired
	private ClientService clientService;

	@GetMapping()
	public ResponseEntity<Page<ClientDTO>> findAllClients(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy) {

		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);

		Page<ClientDTO> clientDtoList = clientService.findAllClientsPaged(pageRequest);
		return ResponseEntity.ok().body(clientDtoList);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> findClientById(@PathVariable Long id) {
		ClientDTO clientDto = clientService.findCliendById(id);
		return ResponseEntity.ok().body(clientDto);
	}

	@PostMapping()
	public ResponseEntity<ClientDTO> addNewClient(@RequestBody ClientDTO clientDto) {
		clientDto = clientService.insertNewClient(clientDto);

		// returning response on headers about new resource created
		URI newResourceUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(clientDto.getId()).toUri();
		return ResponseEntity.created(newResourceUri).body(clientDto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> updateClientInfo(@PathVariable Long id, @RequestBody ClientDTO clientDto) {
		clientDto = clientService.updateClient(id, clientDto);
		return ResponseEntity.ok().body(clientDto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ClientDTO> deleteClient(@PathVariable Long id) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}
}
