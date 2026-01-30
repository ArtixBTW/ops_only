{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
    flake-parts.url = "github:hercules-ci/flake-parts";
    systems.url = "github:nix-systems/default";
  };

  outputs = inputs:
    inputs.flake-parts.lib.mkFlake { inherit inputs; } {
      systems = import inputs.systems;

      perSystem = {  pkgs, lib, ... }: let
        # needed for hot reloading
        # java = pkgs.jetbrains.jdk-no-jcef;
        java = pkgs.jdk21;

        nativeBuildInputs = with pkgs; [
          java
          git
        ];

        buildInputs = with pkgs; [
          libGL
          glfw3-minecraft # Not always needed, but in case it is, it's here.
          flite # TTS
          libpulseaudio # Required for audio
          udev
        ];
      in {
        devShells.default = pkgs.mkShell {
          inherit nativeBuildInputs buildInputs;

          env = {
            LD_LIBRARY_PATH = lib.makeLibraryPath buildInputs;
            JAVA_HOME = "${java.home}";
          };
        };
      };
    };
}
