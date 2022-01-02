package su.nightexpress.excellentcrates.crate.effect;

import org.jetbrains.annotations.NotNull;

public class CrateEffectSettings {

	private CrateEffectModel model;
	private String           particleName;
	private String           particleData;
	
	public CrateEffectSettings(
			@NotNull CrateEffectModel model,
			@NotNull String particleName,
			@NotNull String particleData
			) {
		this.setModel(model);
		this.setParticleName(particleName);
		this.setParticleData(particleData);
	}
	
	@NotNull
	public CrateEffectModel getModel() {
		return this.model;
	}
	
	public void setModel(@NotNull CrateEffectModel model) {
		this.model = model;
	}
	
	@NotNull
	public String getParticleName() {
		return this.particleName;
	}
	
	public void setParticleName(@NotNull String particle) {
		this.particleName = particle;
	}

	@NotNull
	public String getParticleData() {
		return particleData;
	}

	public void setParticleData(@NotNull String particleData) {
		this.particleData = particleData;
	}
}
