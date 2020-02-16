package cn.mccraft.pangu.core.util.font;

import cn.mccraft.pangu.core.PanguCore;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;

public class FontShader {
    private static FontShader INSTANCE = new FontShader();
    private int shaderProg;
    private int shaderProgWithLight;
    private boolean enableShader = true;

    public FontShader() {
        this.setupShader();
    }

    public static FontShader getInstance() {
        return INSTANCE;
    }

    private void setupShader() {
        if(this.enableShader) {
            String vShaderSrc = "#version 120\nvoid main(void){  gl_Position = ftransform();  gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;  gl_FrontColor = gl_Color;}";
            String fShaderSrc = "#version 120\nuniform sampler2D texture;uniform vec4 colorBias;void main(void){  vec4 color = texture2DProj(texture, gl_TexCoord[0]);  color.r = clamp(color.r + colorBias.r , 0.0, 1.0);  color.g = clamp(color.g + colorBias.g , 0.0, 1.0);  color.b = clamp(color.b + colorBias.b , 0.0, 1.0);  color.a = clamp(color.a * colorBias.a , 0.0, 1.0);  gl_FragColor = color * gl_Color;}";
            String vShaderSrc2 = "#version 120\nvoid main(void){  gl_Position = ftransform();  gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;  gl_TexCoord[1] = gl_TextureMatrix[1] * gl_MultiTexCoord1;  gl_FrontColor = gl_Color;}";
            String fShaderSrc2 = "#version 120\nuniform sampler2D texture;uniform sampler2D texture2;uniform vec4 colorBias;void main(void){  vec4 color = texture2DProj(texture, gl_TexCoord[0]);  vec4 color2 = texture2DProj(texture2, gl_TexCoord[1]);  color.r = clamp(color.r + colorBias.r , 0.0, 1.0);  color.g = clamp(color.g + colorBias.g , 0.0, 1.0);  color.b = clamp(color.b + colorBias.b , 0.0, 1.0);  color.a = clamp(color.a * colorBias.a , 0.0, 1.0);  gl_FragColor = color * color2 * gl_Color;}";
            this.shaderProg = this.getShaderProg(vShaderSrc, fShaderSrc);
            this.shaderProgWithLight = this.getShaderProg(vShaderSrc2, fShaderSrc2);
        }
    }

    private int getShaderProg(String vShaderSrc, String fShaderSrc) {
        int shader = GL20.glCreateProgram();
        int vShaderId = GL20.glCreateShader('\u8b31');
        int fShaderId = GL20.glCreateShader('\u8b30');
        GL20.glShaderSource(vShaderId, vShaderSrc);
        GL20.glShaderSource(fShaderId, fShaderSrc);
        GL20.glCompileShader(vShaderId);
        String verr = GL20.glGetShaderInfoLog(vShaderId, 512);
        if(!verr.equals("")) {
            PanguCore.getLogger().error("Vertex shader compile error: " + verr);
        }

        GL20.glCompileShader(fShaderId);
        String ferr = GL20.glGetShaderInfoLog(fShaderId, 512);
        if(!verr.equals("")) {
            PanguCore.getLogger().error("Fragment shader compile error: " + ferr);
        }

        GL20.glAttachShader(shader, fShaderId);
        GL20.glLinkProgram(shader);
        return shader;
    }

    public void setColorBias(boolean lightmap, float r, float g, float b, float a) {
        int colorBiasUniLoc;
        if(lightmap) {
            colorBiasUniLoc = GL20.glGetUniformLocation(this.shaderProgWithLight, "colorBias");
            GL20.glUniform4f(colorBiasUniLoc, r, g, b, a);
        } else {
            colorBiasUniLoc = GL20.glGetUniformLocation(this.shaderProg, "colorBias");
            GL20.glUniform4f(colorBiasUniLoc, r, g, b, a);
        }

    }

    public void useProgram(boolean lightmap) {
        int texLoc;
        if(lightmap) {
            this.useProgram(this.shaderProgWithLight);
            texLoc = GL20.glGetUniformLocation(this.shaderProgWithLight, "texture");
            GL20.glUniform1i(texLoc, OpenGlHelper.defaultTexUnit - GL_TEXTURE0);
            int texLoc2 = GL20.glGetUniformLocation(this.shaderProgWithLight, "texture2");
            GL20.glUniform1i(texLoc2, OpenGlHelper.lightmapTexUnit - GL_TEXTURE0);
        } else {
            this.useProgram(this.shaderProg);
            texLoc = GL20.glGetUniformLocation(this.shaderProg, "texture");
            GL20.glUniform1i(texLoc, OpenGlHelper.defaultTexUnit - GL_TEXTURE0);
        }
    }

    public void useProgram(int prog) {
        if(this.enableShader) {
            while(GL11.glGetError() != 0) {
                ;
            }

            GL20.glUseProgram(prog);
            int errno = GL11.glGetError();
            if(errno != 0) {
                PanguCore.getLogger().error("glUseProgram Error: " + errno);
            }

        }
    }

    public void disuseProgram() {
        if(this.enableShader) {
            GL20.glUseProgram(0);
            if(GL11.glGetError() != 0) {
                PanguCore.getLogger().error("glUseProgram Error.");
            }

        }
    }

    public int getCurProgram() {
        return !this.enableShader?0:GL11.glGetInteger('\u8b8d');
    }

    public boolean checkLightmapTexUnit() {
        GL13.glActiveTexture(OpenGlHelper.lightmapTexUnit);
        boolean ret = GL11.glGetBoolean(3553);
        GL13.glActiveTexture(OpenGlHelper.defaultTexUnit);
        return ret;
    }
}
